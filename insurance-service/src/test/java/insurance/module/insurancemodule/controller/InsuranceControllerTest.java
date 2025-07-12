package insurance.module.insurancemodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // For LocalDateTime

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import insurance.module.insurancemodule.exception.InsuranceIdIsNotFoundException; // Assuming you have this custom exception
import insurance.module.insurancemodule.model.InsuranceDTO;
import insurance.module.insurancemodule.service.InsuranceService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class InsuranceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InsuranceService insuranceService;

    @InjectMocks
    private InsuranceController insuranceController;

    private ObjectMapper objectMapper;

    // DTOs for various scenarios
    private InsuranceDTO predefinedPackageDTO;
    private InsuranceDTO userSelectionDTO;
    private InsuranceDTO newPredefinedTemplateDTO; // For POST /admin/package
    private InsuranceDTO updatedInsuranceDTO; // For PUT /{id}

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(insuranceController).build();

        // Initialize ObjectMapper with JavaTimeModule for LocalDateTime support
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // --- Setup common DTOs for testing ---

        // A predefined package template
        predefinedPackageDTO = InsuranceDTO.builder()
                .insuranceId(101L)
                .userId(null)       // Null for templates
                .bookingId(null)    // Null for templates
                .selectionDate(null) // Null for templates
                .coverageDetails("Basic Travel Cover")
                .provider("TravelSecure Inc.")
                .packageType("Standard")
                .status("PREDEFINED_AVAILABLE")
                .price(500)
                .build();

        // A user's selection based on a predefined package
        userSelectionDTO = InsuranceDTO.builder()
                .insuranceId(201L)
                .userId(1L)
                .bookingId(1001L)
                .selectionDate(LocalDateTime.now().minusDays(5)) // Some past date
                .coverageDetails("Basic Travel Cover")
                .provider("TravelSecure Inc.")
                .packageType("Standard")
                .status("PENDING") // Or PURCHASED
                .price(500)
                .build();

        // DTO for creating a new predefined package template (client-side input)
        newPredefinedTemplateDTO = InsuranceDTO.builder()
                .coverageDetails("Extreme Sports Cover")
                .provider("AdventureSafe")
                .packageType("Extreme")
                .price(1500)
                // Status, userId, bookingId, selectionDate will be set by controller/service
                .build();

        // DTO for updating an existing insurance record
        updatedInsuranceDTO = InsuranceDTO.builder()
                .insuranceId(201L)
                .userId(1L)
                .bookingId(1001L)
                .selectionDate(LocalDateTime.now())
                .coverageDetails("Updated Full Coverage")
                .provider("New Provider Co.")
                .packageType("Premium Plus")
                .status("CONFIRMED")
                .price(1200)
                .build();
    }

    // --- User-Facing Endpoints ---

    @Test
    void testGetAvailablePredefinedPackages() throws Exception {
        List<InsuranceDTO> packages = List.of(
                predefinedPackageDTO,
                InsuranceDTO.builder().insuranceId(102L).packageType("Premium").provider("GlobalSafe").status("PREDEFINED_AVAILABLE").price(1000).build()
        );
        when(insuranceService.getPredefinedPackages()).thenReturn(packages);

        mockMvc.perform(get("/api/insurance/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].packageType").value("Standard"))
                .andExpect(jsonPath("$[1].packageType").value("Premium"));

        verify(insuranceService, times(1)).getPredefinedPackages();
    }

    @Test
    void testSelectPredefinedPackage_Success() throws Exception {
        Long predefinedPackageId = 101L;
        Long userId = 1L;
        Long bookingId = 1001L;

        // Mock the service to return a user selection DTO
        when(insuranceService.selectInsuranceForBooking(eq(predefinedPackageId), eq(userId), eq(bookingId)))
                .thenReturn(userSelectionDTO); // The service returns the new user selection

        mockMvc.perform(post("/api/insurance/select")
                        .param("predefinedPackageId", String.valueOf(predefinedPackageId))
                        .param("userId", String.valueOf(userId))
                        .param("bookingId", String.valueOf(bookingId)))
                .andExpect(status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.insuranceId").value(userSelectionDTO.getInsuranceId()))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.bookingId").value(bookingId))
                .andExpect(jsonPath("$.status").value("PENDING")); // Or whatever status your service sets

        verify(insuranceService, times(1)).selectInsuranceForBooking(predefinedPackageId, userId, bookingId);
    }

    @Test
    void testSelectPredefinedPackage_BadRequest_MissingParams() throws Exception {
        // Missing userId param
        mockMvc.perform(post("/api/insurance/select")
                        .param("predefinedPackageId", "101")
                        .param("bookingId", "1001"))
                .andExpect(status().isBadRequest()); // Expect 400 Bad Request due to @NotNull validation

        verifyNoInteractions(insuranceService); // Service should not be called
    }

    @Test
    void testSelectPredefinedPackage_BadRequest_ServiceThrowsIllegalArgument() throws Exception {
        Long predefinedPackageId = 101L;
        Long userId = 1L;
        Long bookingId = 1001L;

        // Mock service to throw an IllegalArgumentException
        when(insuranceService.selectInsuranceForBooking(anyLong(), anyLong(), anyLong()))
                .thenThrow(new IllegalArgumentException("Invalid package or IDs."));

        mockMvc.perform(post("/api/insurance/select")
                        .param("predefinedPackageId", String.valueOf(predefinedPackageId))
                        .param("userId", String.valueOf(userId))
                        .param("bookingId", String.valueOf(bookingId)))
                .andExpect(status().isBadRequest()); // Expect 400 Bad Request from controller's catch

        verify(insuranceService, times(1)).selectInsuranceForBooking(predefinedPackageId, userId, bookingId);
    }

    @Test
    void testSelectPredefinedPackage_NotFound_ServiceThrowsInsuranceIdNotFound() throws Exception {
        Long nonExistentPackageId = 999L;
        Long userId = 1L;
        Long bookingId = 1001L;

        // Mock service to throw your custom exception
        when(insuranceService.selectInsuranceForBooking(eq(nonExistentPackageId), anyLong(), anyLong()))
                .thenThrow(new InsuranceIdIsNotFoundException("Package not found."));

        mockMvc.perform(post("/api/insurance/select")
                        .param("predefinedPackageId", String.valueOf(nonExistentPackageId))
                        .param("userId", String.valueOf(userId))
                        .param("bookingId", String.valueOf(bookingId)))
                // The controller's catch block for IllegalArgumentException (which InsuranceIdIsNotFoundException extends,
                // or just catching Exception more broadly) will likely turn this into a 500 or 400 if not handled specifically.
                // Assuming `InsuranceIdIsNotFoundException` extends `RuntimeException` and is caught by the generic `Exception` catch,
                // leading to 500. If you want 404, you need an `@ExceptionHandler` or more specific catch.
                .andExpect(status().isInternalServerError()); // Or 400, depending on specific exception handling.

        verify(insuranceService, times(1)).selectInsuranceForBooking(nonExistentPackageId, userId, bookingId);
    }

    // --- Admin/Internal Endpoints ---

    @Test
    void testCreatePredefinedPackageTemplate_Success() throws Exception {
        // Service returns the DTO with generated ID and forced status
        when(insuranceService.createInsurance(any(InsuranceDTO.class))).thenReturn(predefinedPackageDTO);

        String requestJson = objectMapper.writeValueAsString(newPredefinedTemplateDTO);

        mockMvc.perform(post("/api/insurance/admin/package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.insuranceId").value(predefinedPackageDTO.getInsuranceId()))
                .andExpect(jsonPath("$.packageType").value("Standard")) // Should be 'Standard' from predefinedPackageDTO
                .andExpect(jsonPath("$.status").value("PREDEFINED_AVAILABLE")) // Verify forced status
                .andExpect(jsonPath("$.userId").doesNotExist()) // Verify userId is null/not present
                .andExpect(jsonPath("$.bookingId").doesNotExist()); // Verify bookingId is null/not present

        // Verify that the service was called with the modified DTO (status forced)
        verify(insuranceService, times(1)).createInsurance(argThat(dto ->
                dto.getStatus().equals("PREDEFINED_AVAILABLE") &&
                dto.getUserId() == null &&
                dto.getBookingId() == null &&
                dto.getPackageType().equals("Extreme Sports Cover".equals(dto.getCoverageDetails()) ? "Extreme" : dto.getPackageType()) // Check based on input
        ));
    }


    @Test
    void testUpdateInsuranceRecord_Success() throws Exception {
        Long idToUpdate = 201L;

        when(insuranceService.updateInsurance(eq(idToUpdate), any(InsuranceDTO.class))).thenReturn(updatedInsuranceDTO);

        String requestJson = objectMapper.writeValueAsString(updatedInsuranceDTO);

        mockMvc.perform(put("/api/insurance/{id}", idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insuranceId").value(idToUpdate))
                .andExpect(jsonPath("$.coverageDetails").value("Updated Full Coverage"))
                .andExpect(jsonPath("$.provider").value("New Provider Co."))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(insuranceService, times(1)).updateInsurance(eq(idToUpdate), any(InsuranceDTO.class));
    }

    @Test
    void testUpdateInsuranceRecord_NotFound() throws Exception {
        Long nonExistentId = 999L;
        // Mock service to throw exception if ID not found
        when(insuranceService.updateInsurance(eq(nonExistentId), any(InsuranceDTO.class)))
                .thenThrow(new InsuranceIdIsNotFoundException("Insurance ID not found"));

        String requestJson = objectMapper.writeValueAsString(updatedInsuranceDTO); // Content doesn't matter much for 404

        mockMvc.perform(put("/api/insurance/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound()); // Expect 404 Not Found

        verify(insuranceService, times(1)).updateInsurance(eq(nonExistentId), any(InsuranceDTO.class));
    }

    @Test
    void testDeleteInsuranceRecord_Success() throws Exception {
        Long idToDelete = 1L;
        doNothing().when(insuranceService).deleteInsurance(idToDelete);

        mockMvc.perform(delete("/api/insurance/{id}", idToDelete))
                .andExpect(status().isOk());

        verify(insuranceService, times(1)).deleteInsurance(idToDelete);
    }

    @Test
    void testGetInsuranceRecordById_Found() throws Exception {
        Long idToFind = 201L;
        when(insuranceService.getInsuranceById(idToFind)).thenReturn(userSelectionDTO);

        mockMvc.perform(get("/api/insurance/{id}", idToFind))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insuranceId").value(idToFind))
                .andExpect(jsonPath("$.userId").value(userSelectionDTO.getUserId()));

        verify(insuranceService, times(1)).getInsuranceById(idToFind);
    }

    @Test
    void testGetInsuranceRecordById_NotFound() throws Exception {
        Long nonExistentId = 999L;
        when(insuranceService.getInsuranceById(nonExistentId))
                .thenThrow(new InsuranceIdIsNotFoundException("Insurance ID not found"));

        mockMvc.perform(get("/api/insurance/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        verify(insuranceService, times(1)).getInsuranceById(nonExistentId);
    }

    @Test
    void testGetAllInsuranceRecords() throws Exception {
        List<InsuranceDTO> allRecords = List.of(predefinedPackageDTO, userSelectionDTO);
        when(insuranceService.getAllInsurance()).thenReturn(allRecords);

        mockMvc.perform(get("/api/insurance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].packageType").value("Standard"))
                .andExpect(jsonPath("$[1].userId").value(userSelectionDTO.getUserId()));

        verify(insuranceService, times(1)).getAllInsurance();
    }

    @Test
    void testGetInsuranceSelectionsByBookingId() throws Exception {
        Long bookingId = 1001L;
        List<InsuranceDTO> selections = List.of(userSelectionDTO);
        when(insuranceService.getInsuranceSelectionsByBookingId(bookingId)).thenReturn(selections);

        mockMvc.perform(get("/api/insurance/selections/booking/{bookingId}", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].bookingId").value(bookingId))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(insuranceService, times(1)).getInsuranceSelectionsByBookingId(bookingId);
    }

    @Test
    void testGetInsuranceSelectionsByUserId() throws Exception {
        Long userId = 1L;
        List<InsuranceDTO> selections = List.of(userSelectionDTO);
        when(insuranceService.getInsuranceSelectionsByUserId(userId)).thenReturn(selections);

        mockMvc.perform(get("/api/insurance/selections/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].coverageDetails").value("Basic Travel Cover"));

        verify(insuranceService, times(1)).getInsuranceSelectionsByUserId(userId);
    }
}