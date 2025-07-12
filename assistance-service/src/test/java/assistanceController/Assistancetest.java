package assistanceController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any; // Import any()
import static org.mockito.ArgumentMatchers.eq; // Import eq()
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // Import all request builders
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType; // Import MediaType
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature; // For pretty printing/date handling
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // For LocalDateTime serialization

import assistanceModel.AssistanceDTO;
import assistanceService.AssistanceService;

class Assistancetest {

    private MockMvc mockMvc;

    @Mock
    private AssistanceService assistanceRequestService;

    @InjectMocks
    private AssistanceController assistanceRequestController;

    private ObjectMapper objectMapper;

    private AssistanceDTO requestDTO;
    private AssistanceDTO createdRequestDTO; // To store the DTO as if it was created by service
    private AssistanceDTO resolvedRequestDTO; // To store the DTO as if it was resolved by service

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assistanceRequestController).build();

        objectMapper = new ObjectMapper();
        // Register the JavaTimeModule to handle LocalDateTime serialization/deserialization
        objectMapper.registerModule(new JavaTimeModule());
        // Disable WRITE_DATES_AS_TIMESTAMPS to write dates as ISO 8601 strings
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        // DTO for incoming request (customer creates it)
        requestDTO = AssistanceDTO.builder()
                .userId(101L)
                .issueDescription("Need help with claim process")
                .build(); // requestId, status, resolutionTime, resolutionMessage are null/ignored on creation

        // DTO as it would be returned by the service after creation
        createdRequestDTO = AssistanceDTO.builder()
                .requestId(1L)
                .userId(101L)
                .issueDescription("Need help with claim process")
                .status("Pending") // Service sets this
                .build();

        // DTO as it would be returned by the service after resolution
        resolvedRequestDTO = AssistanceDTO.builder()
                .requestId(1L)
                .userId(101L)
                .issueDescription("Need help with claim process")
                .status("Resolved")
                .resolutionTime(LocalDateTime.now()) // Set to current time for testing resolution
                .resolutionMessage("Claim processed successfully.")
                .build();
    }

    @Test
    void testCreateRequest() throws Exception {
        // Mock the service call: when createRequest is called with any AssistanceDTO, return our createdRequestDTO
        when(assistanceRequestService.createRequest(any(AssistanceDTO.class))).thenReturn(createdRequestDTO);

        // Convert the incoming DTO to JSON
        String requestJson = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/api/assistance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.requestId").value(1L))
                .andExpect(jsonPath("$.userId").value(101L))
                .andExpect(jsonPath("$.issueDescription").value("Need help with claim process"))
                .andExpect(jsonPath("$.status").value("Pending")); // Verify status set by service
    }

    @Test
    void testGetRequestByIdFound() throws Exception {
        when(assistanceRequestService.getRequestById(1L)).thenReturn(createdRequestDTO); // Using createdRequestDTO for existing request

        mockMvc.perform(get("/api/assistance/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(1L))
                .andExpect(jsonPath("$.issueDescription").value("Need help with claim process"))
                .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    void testGetRequestByIdNotFound() throws Exception {
        when(assistanceRequestService.getRequestById(99L)).thenReturn(null); // Mock not found

        mockMvc.perform(get("/api/assistance/99"))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found
    }

    @Test
    void testGetAllRequests() throws Exception {
        List<AssistanceDTO> list = Arrays.asList(createdRequestDTO, resolvedRequestDTO); // Example list
        when(assistanceRequestService.getAllRequests()).thenReturn(list);

        mockMvc.perform(get("/api/assistance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(list.size()))
                .andExpect(jsonPath("$[0].requestId").value(1L))
                .andExpect(jsonPath("$[1].status").value("Resolved")); // Check elements in the list
    }

    @Test
    void testResolveAssistanceRequestSuccess() throws Exception {
        String resolutionMessage = "Claim processed successfully.";
        // Mock the service call: when resolveRequest is called with ID 1L and any String, return resolvedRequestDTO
        when(assistanceRequestService.resolveRequest(eq(1L), any(String.class))).thenReturn(resolvedRequestDTO);

        mockMvc.perform(put("/api/assistance/1/resolve")
                        .contentType(MediaType.TEXT_PLAIN) // For @RequestBody String, use TEXT_PLAIN
                        .content(resolutionMessage))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(1L))
                .andExpect(jsonPath("$.status").value("Resolved"))
                .andExpect(jsonPath("$.resolutionMessage").value(resolutionMessage));
        // You might also want to assert that resolutionTime is not null, but checking the exact time is hard
        // .andExpect(jsonPath("$.resolutionTime").exists());
    }

    @Test
    void testResolveAssistanceRequestNotFound() throws Exception {
        String resolutionMessage = "Claim processed successfully.";
        // Mock the service to throw an exception when ID is not found
        when(assistanceRequestService.resolveRequest(eq(99L), any(String.class)))
                .thenThrow(new RuntimeException("Assistance request not found with ID: 99"));

        mockMvc.perform(put("/api/assistance/99/resolve")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(resolutionMessage))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found due to controller's catch block
    }


    @Test
    void testDeleteRequest() throws Exception {
        doNothing().when(assistanceRequestService).deleteRequest(1L);

        mockMvc.perform(delete("/api/assistance/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Assistance request with ID 1 deleted successfully.")); // Corrected spacing
    }
}