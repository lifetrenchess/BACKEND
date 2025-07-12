package assistanceService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import assistanceEntity.AssistanceEntity;
import assistanceModel.AssistanceDTO;
import assistanceRepository.AssistanceRepository;
import cts.assistance.client.UserServiceClient;
import cts.assistance.client.UserDTO;
// import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
// import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import java.time.LocalDateTime; // Import LocalDateTime
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Import for stream API

@Service
public class AssistanceService {

    @Autowired
    private AssistanceRepository requestRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserServiceClient userServiceClient;

    // @Autowired
    // private CircuitBreakerFactory circuitBreakerFactory;

    /**
     * Allows a customer to create a new assistance request.
     * Sets initial status to "Pending" and handles ID generation.
     *
     * @param dto The AssistanceDTO containing userId and issueDescription from the customer.
     * @return The created AssistanceDTO with generated requestId and initial status.
     */
    public AssistanceDTO createRequest(AssistanceDTO dto) {
        // Validate user exists before creating assistance request using circuit breaker
        // CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-service");
        // UserDTO user = circuitBreaker.run(
        //     () -> userServiceClient.getUserById(dto.getUserId()),
        //     throwable -> {
        //         throw new RuntimeException("User service unavailable: " + throwable.getMessage());
        //     }
        // );
        
        // Direct call without circuit breaker
        UserDTO user = userServiceClient.getUserById(dto.getUserId());
        
        if (user == null || user.getUserId() == null) {
            throw new RuntimeException("User not found with ID: " + dto.getUserId());
        }

        // Ensure that client-provided IDs, statuses, and resolution details are ignored on creation.
        dto.setRequestId(null);
        dto.setStatus("Pending"); // Default status for a new request
        dto.setResolutionTime(null);
        dto.setResolutionMessage(null);

        AssistanceEntity request = modelMapper.map(dto, AssistanceEntity.class);
        AssistanceEntity savedRequest = requestRepository.save(request);
        return modelMapper.map(savedRequest, AssistanceDTO.class);
    }

    /**
     * Retrieves a single assistance request by its ID.
     * Can be used by both customers (to check their request status) and admins.
     *
     * @param requestId The ID of the assistance request.
     * @return The AssistanceDTO if found, otherwise null (or throw a custom exception).
     */
    public AssistanceDTO getRequestById(Long requestId) {
        Optional<AssistanceEntity> optional = requestRepository.findById(requestId);
        // Consider throwing ResourceNotFoundException here for better error handling
        return optional.map(entity -> modelMapper.map(entity, AssistanceDTO.class)).orElse(null);
    }

    /**
     * Retrieves all assistance requests.
     * This method is primarily intended for admin users to view all pending/resolved issues.
     *
     * @return A list of all AssistanceDTOs.
     */
    public List<AssistanceDTO> getAllRequests() {
        List<AssistanceEntity> requestList = requestRepository.findAll();
        // Using Stream API for more concise mapping
        return requestList.stream()
                          .map(entity -> modelMapper.map(entity, AssistanceDTO.class))
                          .collect(Collectors.toList());
    }

    /**
     * Allows an admin to resolve a specific assistance request.
     * Updates the status to "Resolved", sets the resolution time, and adds the admin's reply.
     *
     * @param requestId The ID of the request to resolve.
     * @param resolutionMessage The message provided by the admin for resolution.
     * @return The updated AssistanceDTO.
     * @throws RuntimeException if the request with the given ID is not found.
     */
    @Transactional // Ensures the entire operation is atomic
    public AssistanceDTO resolveRequest(Long requestId, String resolutionMessage) {
        AssistanceEntity request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Assistance request not found with ID: " + requestId)); // Better to throw a custom exception here

        request.setStatus("Resolved"); // Set status to Resolved
        request.setResolutionTime(LocalDateTime.now()); // Set resolution time to now
        request.setResolutionMessage(resolutionMessage); // Store the admin's reply

        AssistanceEntity updatedRequest = requestRepository.save(request);
        return modelMapper.map(updatedRequest, AssistanceDTO.class);
    }

    /**
     * Deletes an assistance request by its ID.
     * This method is typically intended only for admin users.
     *
     * @param requestId The ID of the request to delete.
     */
    public void deleteRequest(Long requestId) {
        requestRepository.deleteById(requestId);
    }
}