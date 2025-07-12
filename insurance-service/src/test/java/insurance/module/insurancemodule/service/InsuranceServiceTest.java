package insurance.module.insurancemodule.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat; // Import argThat
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import insurance.module.insurancemodule.entity.Insurance;
import insurance.module.insurancemodule.exception.InsuranceIdIsNotFoundException;
import insurance.module.insurancemodule.model.InsuranceDTO;
import insurance.module.insurancemodule.repository.InsuranceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class InsuranceServiceTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private InsuranceRepository insuranceRepository;

    @InjectMocks
    private InsuranceService insuranceService;

    // DTOs and Entities for various test scenarios
    private InsuranceDTO predefinedPackageDTO;
    private Insurance predefinedPackageEntity;

    private InsuranceDTO userSelectionPendingDTO;
    private Insurance userSelectionPendingEntity;

    private InsuranceDTO newTemplateInputDTO; // DTO that comes into createPredefinedPackageTemplate
    // private Insurance newTemplateCreatedEntity; // Removed, handled by mock answers for better clarity
    private InsuranceDTO newTemplateCreatedDTO; // DTO that is returned after creation

    @BeforeEach
    void setUp() {
        // 1. Predefined Package (Template)
        predefinedPackageDTO = InsuranceDTO.builder()
                .insuranceId(100L)
                .coverageDetails("Basic Coverage")
                .provider("TestInsure")
                .packageType("Economy")
                .status("PREDEFINED_AVAILABLE")
                .price(100)
                .build();

        predefinedPackageEntity = Insurance.builder()
                .insuranceId(100L)
                .coverageDetails("Basic Coverage")
                .provider("TestInsure")
                .packageType("Economy")
                .status("PREDEFINED_AVAILABLE")
                .price(100)
                .build();

        // 2. User's Insurance Selection (e.g., after selecting a predefined package)
        // Note: For actual service logic, selectionDate will be set by the service
        // For test setup, we can populate it if needed for verification in DTO
        userSelectionPendingDTO = InsuranceDTO.builder()
                .insuranceId(200L)
                .userId(1001L)
                .bookingId(2001L)
                .coverageDetails("Basic Coverage") // From predefined
                .provider("TestInsure")            // From predefined
                .packageType("Economy")            // From predefined
                .status("PENDING")                 // Set by service
                .price(100)                        // From predefined
                .selectionDate(LocalDateTime.now().minusMinutes(1)) // For test setup, assume a past time
                .build();

        userSelectionPendingEntity = Insurance.builder()
                .insuranceId(200L)
                .userId(1001L)
                .bookingId(2001L)
                .coverageDetails("Basic Coverage")
                .provider("TestInsure")
                .packageType("Economy")
                .status("PENDING")
                .price(100)
                .selectionDate(LocalDateTime.now().minusMinutes(1))
                .build();

        // 3. Input DTO for creating a new predefined template (no ID, status, user/booking)
        newTemplateInputDTO = InsuranceDTO.builder()
                .coverageDetails("New Template Coverage")
                .provider("New Provider")
                .packageType("Custom")
                .price(500)
                .build();

        // 5. DTO returned by service for a new template (after service processing)
        newTemplateCreatedDTO = InsuranceDTO.builder()
                .insuranceId(300L) // Assuming ID is generated
                .coverageDetails("New Template Coverage")
                .provider("New Provider")
                .packageType("Custom")
                .status("PREDEFINED_AVAILABLE")
                .price(500)
                .build();
    }

    // --- Core CRUD (Admin/Internal) ---

    @Test
    void testCreateInsurance_GeneralPurpose() {
        // Given
        InsuranceDTO inputDto = InsuranceDTO.builder()
                .coverageDetails("Standard Plan")
                .provider("XYZ Corp")
                .packageType("Standard")
                .status("ACTIVE")
                .price(750)
                .build();

        // Entity that the mapper will produce from inputDto
        Insurance mappedEntityFromInput = Insurance.builder()
                .coverageDetails("Standard Plan")
                .provider("XYZ Corp")
                .packageType("Standard")
                .status("ACTIVE")
                .price(750)
                .build();

        // Entity that the repository will return after saving (with generated ID)
        Insurance savedEntityFromRepo = Insurance.builder()
                .insuranceId(1L) // Simulate ID generation
                .coverageDetails("Standard Plan")
                .provider("XYZ Corp")
                .packageType("Standard")
                .status("ACTIVE")
                .price(750)
                .build();

        // The DTO we expect to be returned by the service, after mapping the saved entity
        InsuranceDTO expectedReturnedDto = InsuranceDTO.builder()
                .insuranceId(1L)
                .coverageDetails("Standard Plan")
                .provider("XYZ Corp")
                .packageType("Standard")
                .status("ACTIVE")
                .price(750)
                .build();

        // When
        // 1. Mock mapper behavior for DTO to Entity conversion
        when(mapper.map(inputDto, Insurance.class)).thenReturn(mappedEntityFromInput);

        // 2. Mock repository behavior for saving the entity
        when(insuranceRepository.save(any(Insurance.class))).thenReturn(savedEntityFromRepo); // Return the entity with the simulated ID

        // 3. Mock mapper behavior for Entity to DTO conversion (the final step in your service)
        when(mapper.map(savedEntityFromRepo, InsuranceDTO.class)).thenReturn(expectedReturnedDto);

        // Call the service method
        InsuranceDTO result = insuranceService.createInsurance(inputDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getInsuranceId());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals("Standard Plan", result.getCoverageDetails());

        // Verify interactions
        verify(mapper, times(1)).map(inputDto, Insurance.class);
        verify(insuranceRepository, times(1)).save(mappedEntityFromInput); // Verify the entity that was passed to save
        verify(mapper, times(1)).map(savedEntityFromRepo, InsuranceDTO.class);
    }

    @Test
    void testCreateInsurance_PredefinedTemplateStatusForced() {
        // When creating a predefined template, the service *should* ensure userId/bookingId/selectionDate are null
        // even if they were present in the input DTO (though the controller already nulls them for POST /admin/package)
        // This test simulates a direct call to the service without controller preprocessing.
        newTemplateInputDTO.setStatus("SOME_RANDOM_STATUS"); // Service should override this
        newTemplateInputDTO.setUserId(99L); // Service should nullify this
        newTemplateInputDTO.setBookingId(999L); // Service should nullify this
        newTemplateInputDTO.setSelectionDate(LocalDateTime.now()); // Service should nullify this

        // Mock the mapping from input DTO to entity (before service modifies it)
        when(mapper.map(any(InsuranceDTO.class), eq(Insurance.class)))
            .thenAnswer(invocation -> {
                // Create an entity reflecting what mapper.map(dto, entity.class) would initially produce
                InsuranceDTO input = invocation.getArgument(0);
                return Insurance.builder()
                    .coverageDetails(input.getCoverageDetails())
                    .provider(input.getProvider())
                    .packageType(input.getPackageType())
                    .status(input.getStatus()) // This status will be overwritten by service
                    .price(input.getPrice())
                    .userId(input.getUserId()) // These will be nullified
                    .bookingId(input.getBookingId()) // These will be nullified
                    .selectionDate(input.getSelectionDate()) // These will be nullified
                    .build();
            });

        // Mock the save, it receives the modified entity and returns it
        when(insuranceRepository.save(any(Insurance.class)))
            .thenAnswer(invocation -> {
                Insurance capturedEntity = invocation.getArgument(0);
                // The entity captured here should have the status, userId, bookingId, selectionDate set correctly by the service
                capturedEntity.setInsuranceId(300L); // Simulate ID generation for the saved entity
                return capturedEntity; // Return the *modified* entity
            });


        when(mapper.map(any(Insurance.class), eq(InsuranceDTO.class)))
            .thenReturn(newTemplateCreatedDTO); // This DTO should reflect the expected final state

        InsuranceDTO result = insuranceService.createInsurance(newTemplateInputDTO);

        assertNotNull(result);
        assertEquals("PREDEFINED_AVAILABLE", result.getStatus());
        assertNull(result.getUserId());
        assertNull(result.getBookingId());
        assertNull(result.getSelectionDate()); // Ensure selectionDate is null for templates
        assertEquals(300L, result.getInsuranceId()); // Verify ID generated
        assertEquals("New Template Coverage", result.getCoverageDetails());

        // Verify save was called with an entity that has the correct state
        verify(insuranceRepository, times(1)).save(argThat(
            entity -> "PREDEFINED_AVAILABLE".equals(entity.getStatus()) &&
                      entity.getUserId() == null &&
                      entity.getBookingId() == null &&
                      entity.getSelectionDate() == null
        ));
        verify(mapper, times(1)).map(newTemplateInputDTO, Insurance.class); // Verify the input mapping
        verify(mapper, times(1)).map(any(Insurance.class), eq(InsuranceDTO.class)); // Verify the output mapping
    }


    @Test
    void testGetInsuranceById_Found() {
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.of(userSelectionPendingEntity));
        when(mapper.map(userSelectionPendingEntity, InsuranceDTO.class)).thenReturn(userSelectionPendingDTO);

        InsuranceDTO result = insuranceService.getInsuranceById(200L);

        assertNotNull(result);
        assertEquals(200L, result.getInsuranceId());
        assertEquals("Basic Coverage", result.getCoverageDetails());
        assertEquals(1001L, result.getUserId());
        verify(insuranceRepository, times(1)).findById(200L);
        verify(mapper, times(1)).map(userSelectionPendingEntity, InsuranceDTO.class);
    }

    @Test
    void testGetInsuranceById_ThrowsException() {
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InsuranceIdIsNotFoundException.class, () -> {
            insuranceService.getInsuranceById(9999L);
        });
        verify(insuranceRepository, times(1)).findById(9999L);
        verify(mapper, never()).map(any(Insurance.class), any(Class.class));
    }

    @Test
    void testUpdateInsurance_Found() {
        InsuranceDTO updateInputDto = InsuranceDTO.builder()
                .coverageDetails("Updated Full Coverage")
                .provider("New Provider")
                .packageType("Platinum")
                .status("PURCHASED")
                .price(1500)
                .userId(1001L) // Keep existing user ID
                .bookingId(2001L) // Keep existing booking ID
                .selectionDate(LocalDateTime.now().minusDays(1)) // Update selection date
                .build();

        // The original entity fetched from the DB
        Insurance existingEntity = Insurance.builder()
                .insuranceId(userSelectionPendingEntity.getInsuranceId())
                .userId(userSelectionPendingEntity.getUserId())
                .bookingId(userSelectionPendingEntity.getBookingId())
                .coverageDetails(userSelectionPendingEntity.getCoverageDetails())
                .provider(userSelectionPendingEntity.getProvider())
                .packageType(userSelectionPendingEntity.getPackageType())
                .status(userSelectionPendingEntity.getStatus())
                .price(userSelectionPendingEntity.getPrice())
                .selectionDate(userSelectionPendingEntity.getSelectionDate())
                .build();

        // Entity that the repository will return after saving (reflecting updates)
        Insurance savedUpdatedEntity = Insurance.builder()
                .insuranceId(existingEntity.getInsuranceId())
                .userId(updateInputDto.getUserId())
                .bookingId(updateInputDto.getBookingId())
                .coverageDetails(updateInputDto.getCoverageDetails())
                .provider(updateInputDto.getProvider())
                .packageType(updateInputDto.getPackageType())
                .status(updateInputDto.getStatus())
                .price(updateInputDto.getPrice())
                .selectionDate(updateInputDto.getSelectionDate())
                .build();

        // The DTO we expect to be returned by the service
        InsuranceDTO expectedUpdatedOutputDto = InsuranceDTO.builder()
                .insuranceId(existingEntity.getInsuranceId())
                .userId(updateInputDto.getUserId())
                .bookingId(updateInputDto.getBookingId())
                .coverageDetails(updateInputDto.getCoverageDetails())
                .provider(updateInputDto.getProvider())
                .packageType(updateInputDto.getPackageType())
                .status(updateInputDto.getStatus())
                .price(updateInputDto.getPrice())
                .selectionDate(updateInputDto.getSelectionDate())
                .build();


        when(insuranceRepository.findById(existingEntity.getInsuranceId())).thenReturn(Optional.of(existingEntity));
        when(insuranceRepository.save(any(Insurance.class))).thenReturn(savedUpdatedEntity);
        when(mapper.map(savedUpdatedEntity, InsuranceDTO.class)).thenReturn(expectedUpdatedOutputDto);


        InsuranceDTO result = insuranceService.updateInsurance(existingEntity.getInsuranceId(), updateInputDto);

        assertNotNull(result);
        assertEquals("PURCHASED", result.getStatus());
        assertEquals("Platinum", result.getPackageType());
        assertEquals("New Provider", result.getProvider());
        assertEquals(1500, result.getPrice());
        assertEquals(1001L, result.getUserId());
        assertNotNull(result.getSelectionDate());

        // Verify save was called with an entity that reflects the updates
        verify(insuranceRepository, times(1)).save(argThat(entity ->
            entity.getStatus().equals("PURCHASED") &&
            entity.getPackageType().equals("Platinum") &&
            entity.getCoverageDetails().equals("Updated Full Coverage") &&
            entity.getUserId().equals(1001L) &&
            entity.getSelectionDate() != null
        ));
        verify(insuranceRepository, times(1)).findById(existingEntity.getInsuranceId());
        verify(mapper, times(1)).map(savedUpdatedEntity, InsuranceDTO.class);
    }

    @Test
    void testUpdateInsurance_NotFoundThrowsException() {
        InsuranceDTO dummyDto = InsuranceDTO.builder().build();
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InsuranceIdIsNotFoundException.class, () -> {
            insuranceService.updateInsurance(9999L, dummyDto);
        });
        verify(insuranceRepository, times(1)).findById(9999L);
        verify(insuranceRepository, never()).save(any(Insurance.class));
        verify(mapper, never()).map(any(Insurance.class), any(Class.class));
    }


    @Test
    void testDeleteInsurance_Success() {
        // Mock findById to ensure the entity exists before deletion
        when(insuranceRepository.findById(100L)).thenReturn(Optional.of(predefinedPackageEntity));
        doNothing().when(insuranceRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> insuranceService.deleteInsurance(100L));
        verify(insuranceRepository, times(1)).findById(100L); // Verify existence check
        verify(insuranceRepository, times(1)).deleteById(100L);
    }

    @Test
    void testDeleteInsurance_NotFoundThrowsException() {
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InsuranceIdIsNotFoundException.class, () -> {
            insuranceService.deleteInsurance(9999L);
        });
        verify(insuranceRepository, times(1)).findById(9999L);
        verify(insuranceRepository, never()).deleteById(anyLong()); // Ensure deleteById is not called
    }

    // --- New Service Methods ---

    @Test
    void testGetPredefinedPackages_Success() {
        Insurance predefined2 = Insurance.builder().insuranceId(101L).packageType("Business").status("PREDEFINED_AVAILABLE").build();

        // Create DTOs that mapper.map() will return
        InsuranceDTO predefined2DTO = InsuranceDTO.builder().insuranceId(101L).packageType("Business").status("PREDEFINED_AVAILABLE").build();


        List<Insurance> mockEntities = Arrays.asList(predefinedPackageEntity, predefined2);

        when(insuranceRepository.findByStatus("PREDEFINED_AVAILABLE")).thenReturn(mockEntities);
        when(mapper.map(predefinedPackageEntity, InsuranceDTO.class)).thenReturn(predefinedPackageDTO);
        when(mapper.map(predefined2, InsuranceDTO.class)).thenReturn(predefined2DTO);

        List<InsuranceDTO> result = insuranceService.getPredefinedPackages();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getPackageType().equals("Economy")));
        assertTrue(result.stream().anyMatch(dto -> dto.getPackageType().equals("Business")));
        assertFalse(result.stream().anyMatch(dto -> dto.getStatus().equals("PENDING"))); // Ensure no user selections are returned
        verify(insuranceRepository, times(1)).findByStatus("PREDEFINED_AVAILABLE");
        verify(mapper, times(2)).map(any(Insurance.class), eq(InsuranceDTO.class));
    }

    @Test
    void testSelectInsuranceForBooking_Success() {
        Long selectedPackageId = predefinedPackageEntity.getInsuranceId();
        Long userId = 300L;
        Long bookingId = 400L;

        // Service fetches the template
        when(insuranceRepository.findById(selectedPackageId)).thenReturn(Optional.of(predefinedPackageEntity));

        // Mock `insuranceRepository.save` to return an entity that simulates the saved state
        when(insuranceRepository.save(any(Insurance.class))).thenAnswer(invocation -> {
            Insurance savedSelection = invocation.getArgument(0);
            savedSelection.setInsuranceId(500L); // Simulate ID generation for the new selection
            return savedSelection;
        });

        // Mock `mapper.map` for the final conversion from saved entity to DTO
        when(mapper.map(any(Insurance.class), eq(InsuranceDTO.class))).thenAnswer(invocation -> {
            Insurance savedEntity = invocation.getArgument(0);
            // Construct the DTO based on the state of the *savedEntity*
            return InsuranceDTO.builder()
                    .insuranceId(savedEntity.getInsuranceId())
                    .userId(savedEntity.getUserId())
                    .bookingId(savedEntity.getBookingId())
                    .coverageDetails(savedEntity.getCoverageDetails())
                    .provider(savedEntity.getProvider())
                    .packageType(savedEntity.getPackageType())
                    .status(savedEntity.getStatus())
                    .price(savedEntity.getPrice())
                    .selectionDate(savedEntity.getSelectionDate())
                    .build();
        });


        InsuranceDTO result = insuranceService.selectInsuranceForBooking(selectedPackageId, userId, bookingId);

        assertNotNull(result);
        assertEquals(500L, result.getInsuranceId()); // Generated ID
        assertEquals(userId, result.getUserId());
        assertEquals(bookingId, result.getBookingId());
        assertEquals("PENDING", result.getStatus()); // Service sets to PENDING
        assertNotNull(result.getSelectionDate()); // Service sets current date/time
        assertEquals(predefinedPackageDTO.getCoverageDetails(), result.getCoverageDetails());
        assertEquals(predefinedPackageDTO.getProvider(), result.getProvider());
        assertEquals(predefinedPackageDTO.getPackageType(), result.getPackageType());
        assertEquals(predefinedPackageDTO.getPrice(), result.getPrice());

        verify(insuranceRepository, times(1)).findById(selectedPackageId);
        verify(insuranceRepository, times(1)).save(any(Insurance.class));
        verify(mapper, times(1)).map(any(Insurance.class), eq(InsuranceDTO.class));
    }

    @Test
    void testSelectInsuranceForBooking_ThrowsIllegalArgumentException_MissingParameters() {
        // Test missing userId
        assertThrows(IllegalArgumentException.class, () ->
            insuranceService.selectInsuranceForBooking(100L, null, 200L));

        // Test missing bookingId
        assertThrows(IllegalArgumentException.class, () ->
            insuranceService.selectInsuranceForBooking(100L, 1L, null));

        // Test missing predefinedPackageId
        assertThrows(IllegalArgumentException.class, () ->
            insuranceService.selectInsuranceForBooking(null, 1L, 200L));

        verifyNoInteractions(insuranceRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void testSelectInsuranceForBooking_ThrowsInsuranceIdIsNotFoundException_PackageNotFound() {
        Long nonExistentPackageId = 999L;
        when(insuranceRepository.findById(nonExistentPackageId)).thenReturn(Optional.empty());

        assertThrows(InsuranceIdIsNotFoundException.class, () ->
            insuranceService.selectInsuranceForBooking(nonExistentPackageId, 1L, 2L));

        verify(insuranceRepository, times(1)).findById(nonExistentPackageId);
        verify(insuranceRepository, never()).save(any(Insurance.class));
        verify(mapper, never()).map(any(Insurance.class), any(Class.class));
    }

    @Test
    void testSelectInsuranceForBooking_ThrowsIllegalArgumentException_NotPredefinedPackage() {
        // Simulate finding an existing user selection instead of a predefined template
        Insurance existingUserSelection = Insurance.builder()
                .insuranceId(200L)
                .userId(1L)
                .bookingId(2L)
                .status("PURCHASED") // Not "PREDEFINED_AVAILABLE"
                .build();

        when(insuranceRepository.findById(existingUserSelection.getInsuranceId())).thenReturn(Optional.of(existingUserSelection));

        assertThrows(IllegalArgumentException.class, () ->
            insuranceService.selectInsuranceForBooking(existingUserSelection.getInsuranceId(), 1L, 2L));

        verify(insuranceRepository, times(1)).findById(existingUserSelection.getInsuranceId());
        verify(insuranceRepository, never()).save(any(Insurance.class));
        verify(mapper, never()).map(any(Insurance.class), any(Class.class));
    }


    @Test
    void testGetAllInsurance_ReturnsAllRecords() {
        Insurance insurance1 = predefinedPackageEntity;
        Insurance insurance2 = userSelectionPendingEntity;
        Insurance insurance3 = Insurance.builder().insuranceId(300L).status("CANCELLED").build();

        List<Insurance> allEntities = Arrays.asList(insurance1, insurance2, insurance3);
        // We will mock mapper.map calls for each specific entity
        // List<InsuranceDTO> allDTOs = allEntities.stream().map(e -> mapper.map(e, InsuranceDTO.class)).collect(Collectors.toList());


        when(insuranceRepository.findAll()).thenReturn(allEntities);
        // Mock individual mappings for precise control
        when(mapper.map(insurance1, InsuranceDTO.class)).thenReturn(predefinedPackageDTO);
        when(mapper.map(insurance2, InsuranceDTO.class)).thenReturn(userSelectionPendingDTO);
        when(mapper.map(insurance3, InsuranceDTO.class)).thenReturn(InsuranceDTO.builder().insuranceId(300L).status("CANCELLED").build());


        List<InsuranceDTO> result = insuranceService.getAllInsurance();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(i -> i.getInsuranceId().equals(100L) && i.getStatus().equals("PREDEFINED_AVAILABLE")));
        assertTrue(result.stream().anyMatch(i -> i.getInsuranceId().equals(200L) && i.getStatus().equals("PENDING")));
        assertTrue(result.stream().anyMatch(i -> i.getInsuranceId().equals(300L) && i.getStatus().equals("CANCELLED")));

        verify(insuranceRepository, times(1)).findAll();
        // Verify mapper was called for each entity. The `any(Insurance.class)` is fine here.
        verify(mapper, times(3)).map(any(Insurance.class), eq(InsuranceDTO.class));
    }


    @Test
    void testGetInsuranceSelectionsByBookingId_Success() {
        Long targetBookingId = 2001L;
        Insurance selection1 = userSelectionPendingEntity; // Booking 2001, status PENDING
        Insurance selection2 = Insurance.builder().insuranceId(202L).userId(1001L).bookingId(2001L).status("PURCHASED").build();

        List<Insurance> expectedFilteredEntities = Arrays.asList(selection1, selection2);

        when(insuranceRepository.findByBookingIdAndStatusNot(targetBookingId, "PREDEFINED_AVAILABLE"))
              .thenReturn(expectedFilteredEntities);

        when(mapper.map(selection1, InsuranceDTO.class)).thenReturn(userSelectionPendingDTO);
        when(mapper.map(selection2, InsuranceDTO.class)).thenReturn(InsuranceDTO.builder().insuranceId(202L).userId(1001L).bookingId(2001L).status("PURCHASED").build());


        List<InsuranceDTO> result = insuranceService.getInsuranceSelectionsByBookingId(targetBookingId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getBookingId().equals(targetBookingId)));
        assertFalse(result.stream().anyMatch(s -> "PREDEFINED_AVAILABLE".equals(s.getStatus())));

        verify(insuranceRepository, times(1)).findByBookingIdAndStatusNot(targetBookingId, "PREDEFINED_AVAILABLE");
        verify(mapper, times(2)).map(any(Insurance.class), eq(InsuranceDTO.class));
    }

    @Test
    void testGetInsuranceSelectionsByUserId_Success() {
        Long targetUserId = 1001L;
        Insurance selection1 = userSelectionPendingEntity; // User 1001, status PENDING
        Insurance selection2 = Insurance.builder().insuranceId(203L).userId(1001L).bookingId(2002L).status("CANCELLED").build();

        List<Insurance> expectedFilteredEntities = Arrays.asList(selection1, selection2);

        when(insuranceRepository.findByUserIdAndStatusNot(targetUserId, "PREDEFINED_AVAILABLE"))
              .thenReturn(expectedFilteredEntities);

        when(mapper.map(selection1, InsuranceDTO.class)).thenReturn(userSelectionPendingDTO);
        when(mapper.map(selection2, InsuranceDTO.class)).thenReturn(InsuranceDTO.builder().insuranceId(203L).userId(1001L).bookingId(2002L).status("CANCELLED").build());


        List<InsuranceDTO> result = insuranceService.getInsuranceSelectionsByUserId(targetUserId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getUserId().equals(targetUserId)));
        assertFalse(result.stream().anyMatch(s -> "PREDEFINED_AVAILABLE".equals(s.getStatus())));

        verify(insuranceRepository, times(1)).findByUserIdAndStatusNot(targetUserId, "PREDEFINED_AVAILABLE");
        verify(mapper, times(2)).map(any(Insurance.class), eq(InsuranceDTO.class));
    }
}