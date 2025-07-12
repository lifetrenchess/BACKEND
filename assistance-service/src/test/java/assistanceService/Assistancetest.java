package assistanceService;
 // Correct package name for the test class

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import assistanceEntity.AssistanceEntity;
import assistanceModel.AssistanceDTO;
import assistanceRepository.AssistanceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import assistanceService.AssistanceService;

@ExtendWith(MockitoExtension.class)
class AssistanceServiceTest {

    @Mock
    private AssistanceRepository requestRepository; // Match the name in AssistanceService

    @Mock
    private ModelMapper modelMapper; // Match the name in AssistanceService

    @InjectMocks
    private AssistanceService assistanceService; // Match the name of the service class

    private AssistanceDTO inputAssistanceDTO;    // DTO received from the controller for creation/update
    private AssistanceEntity assistanceEntity;   // Entity saved/retrieved from the repository
    private AssistanceDTO returnedAssistanceDTO; // DTO returned by the service after an operation

    @BeforeEach
    void setUp() {
        // DTO that would come from the controller/client for a new request
        inputAssistanceDTO = AssistanceDTO.builder()
                .userId(101L)
                .issueDescription("My flight was delayed, need support.")
                .build();

        // Entity as it would be saved/retrieved from the database (with auto-generated ID, and service-set status)
        assistanceEntity = AssistanceEntity.builder()
                .requestId(1L)
                .userId(101L)
                .issueDescription("My flight was delayed, need support.")
                .status("Pending") // Service sets this default status
                .build();

        // DTO that the service would return after successful creation
        returnedAssistanceDTO = AssistanceDTO.builder()
                .requestId(1L)
                .userId(101L)
                .issueDescription("My flight was delayed, need support.")
                .status("Pending")
                .build();
    }

    @Test
    void testCreateRequest_Success() {
        // Mock ModelMapper: DTO to Entity
        when(modelMapper.map(inputAssistanceDTO, AssistanceEntity.class)).thenReturn(assistanceEntity);
        // Mock Repository: save operation
        when(requestRepository.save(any(AssistanceEntity.class))).thenReturn(assistanceEntity);
        // Mock ModelMapper: Entity to DTO for return
        when(modelMapper.map(assistanceEntity, AssistanceDTO.class)).thenReturn(returnedAssistanceDTO);

        // Call the service method
        AssistanceDTO result = assistanceService.createRequest(inputAssistanceDTO);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getRequestId());
        assertEquals(101L, result.getUserId());
        assertEquals("My flight was delayed, need support.", result.getIssueDescription());
        assertEquals("Pending", result.getStatus()); // Verify default status is set
        assertNull(result.getResolutionTime());
        assertNull(result.getResolutionMessage());

        // Verify interactions
        verify(modelMapper, times(1)).map(inputAssistanceDTO, AssistanceEntity.class);
        verify(requestRepository, times(1)).save(any(AssistanceEntity.class)); // Verifies save was called
        verify(modelMapper, times(1)).map(assistanceEntity, AssistanceDTO.class);
    }

    @Test
    void testGetRequestById_Found() {
        // Mock Repository: findById returns an Optional containing the entity
        when(requestRepository.findById(1L)).thenReturn(Optional.of(assistanceEntity));
        // Mock ModelMapper: Entity to DTO for return
        when(modelMapper.map(assistanceEntity, AssistanceDTO.class)).thenReturn(returnedAssistanceDTO);

        // Call the service method
        AssistanceDTO result = assistanceService.getRequestById(1L);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getRequestId());
        assertEquals("My flight was delayed, need support.", result.getIssueDescription());
        assertEquals("Pending", result.getStatus());

        // Verify interactions
        verify(requestRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(assistanceEntity, AssistanceDTO.class);
    }

    @Test
    void testGetRequestById_NotFound() {
        // Mock Repository: findById returns an empty Optional
        when(requestRepository.findById(99L)).thenReturn(Optional.empty());

        // Call the service method and expect null (as per your service method)
        AssistanceDTO result = assistanceService.getRequestById(99L);

        // Assertions
        assertNull(result); // Service returns null if not found

        // Verify interactions
        verify(requestRepository, times(1)).findById(99L);
        // modelMapper should not be called if entity is not found
        verify(modelMapper, never()).map(any(AssistanceEntity.class), any(Class.class));
    }

    @Test
    void testGetAllRequests_Success() {
        List<AssistanceEntity> entityList = Arrays.asList(assistanceEntity,
            AssistanceEntity.builder().requestId(2L).userId(102L).issueDescription("Lost luggage").status("Resolved").build()
        );
        List<AssistanceDTO> dtoList = Arrays.asList(returnedAssistanceDTO,
            AssistanceDTO.builder().requestId(2L).userId(102L).issueDescription("Lost luggage").status("Resolved").build()
        );

        // Mock Repository: findAll returns a list of entities
        when(requestRepository.findAll()).thenReturn(entityList);
        // Mock ModelMapper: for each entity in the stream, map to DTO
        // Use an ArgumentMatcher for the 'map' method when dealing with streams.
        // This simulates mapping each entity to its corresponding DTO.
        when(modelMapper.map(entityList.get(0), AssistanceDTO.class)).thenReturn(dtoList.get(0));
        when(modelMapper.map(entityList.get(1), AssistanceDTO.class)).thenReturn(dtoList.get(1));


        // Call the service method
        List<AssistanceDTO> results = assistanceService.getAllRequests();

        // Assertions
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getRequestId());
        assertEquals("Lost luggage", results.get(1).getIssueDescription());
        assertEquals("Resolved", results.get(1).getStatus());


        // Verify interactions
        verify(requestRepository, times(1)).findAll();
        // Verify modelMapper was called twice, once for each entity
        verify(modelMapper, times(2)).map(any(AssistanceEntity.class), eq(AssistanceDTO.class));
    }


    @Test
    void testResolveRequest_Success() {
        String resolutionMessage = "Issue resolved by providing a refund.";
        LocalDateTime beforeResolution = LocalDateTime.now();

        AssistanceEntity resolvedAssistanceEntity = AssistanceEntity.builder()
                .requestId(1L)
                .userId(101L)
                .issueDescription("My flight was delayed, need support.")
                .status("Resolved") // Service sets this
                .resolutionTime(LocalDateTime.now()) // Service sets this
                .resolutionMessage(resolutionMessage) // Service sets this
                .build();

        AssistanceDTO resolvedAssistanceDTO = AssistanceDTO.builder()
                .requestId(1L)
                .userId(101L)
                .issueDescription("My flight was delayed, need support.")
                .status("Resolved")
                .resolutionTime(LocalDateTime.now()) // Will be asserted for not null
                .resolutionMessage(resolutionMessage)
                .build();


        // Mock Repository: findById returns the existing entity
        when(requestRepository.findById(1L)).thenReturn(Optional.of(assistanceEntity)); // Use the initial entity
        // Mock Repository: save returns the updated entity
        when(requestRepository.save(any(AssistanceEntity.class))).thenReturn(resolvedAssistanceEntity);
        // Mock ModelMapper: updated Entity to DTO for return
        when(modelMapper.map(any(AssistanceEntity.class), eq(AssistanceDTO.class))).thenReturn(resolvedAssistanceDTO);

        // Call the service method
        AssistanceDTO result = assistanceService.resolveRequest(1L, resolutionMessage);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getRequestId());
        assertEquals("Resolved", result.getStatus());
        assertEquals(resolutionMessage, result.getResolutionMessage());
        assertNotNull(result.getResolutionTime()); // Ensure resolution time is set
        assertTrue(result.getResolutionTime().isAfter(beforeResolution) || result.getResolutionTime().isEqual(beforeResolution));


        // Verify interactions
        verify(requestRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).save(any(AssistanceEntity.class)); // Verify save was called
        verify(modelMapper, times(1)).map(any(AssistanceEntity.class), eq(AssistanceDTO.class));
    }

    @Test
    void testResolveRequest_NotFoundThrowsException() {
        String resolutionMessage = "Issue resolved by providing a refund.";

        // Mock Repository: findById returns an empty Optional
        when(requestRepository.findById(99L)).thenReturn(Optional.empty());

        // Call the service method and expect RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assistanceService.resolveRequest(99L, resolutionMessage);
        });

        // Assert the exception message
        assertEquals("Assistance request not found with ID: 99", exception.getMessage());

        // Verify interactions
        verify(requestRepository, times(1)).findById(99L);
        // save and map should not be called if entity is not found
        verify(requestRepository, never()).save(any(AssistanceEntity.class));
        verify(modelMapper, never()).map(any(AssistanceEntity.class), any(Class.class));
    }

    @Test
    void testDeleteRequest_Success() {
        // Mock Repository: deleteById does nothing
        doNothing().when(requestRepository).deleteById(1L);

        // Call the service method
        assertDoesNotThrow(() -> assistanceService.deleteRequest(1L));

        // Verify interactions
        verify(requestRepository, times(1)).deleteById(1L);
    }
}