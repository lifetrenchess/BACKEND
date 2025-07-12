package assistanceController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Import HttpStatus for detailed responses
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import assistanceModel.AssistanceDTO;
import assistanceService.AssistanceService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assistance")
public class AssistanceController {

    @Autowired
    private AssistanceService assistanceRequestService;

    /**
     * Endpoint for customers to create a new assistance request.
     * Maps to POST /api/assistance
     *
     * @param dto The AssistanceDTO containing userId and issueDescription from the customer.
     * @return ResponseEntity with the created AssistanceDTO and HTTP 201 Created status.
     */
    @PostMapping
    public ResponseEntity<AssistanceDTO> createRequest(@Valid @RequestBody AssistanceDTO dto) {
        // As a security measure and to ensure data integrity,
        // explicitly nullify fields that should not be set by the customer on creation.
        dto.setRequestId(null);
        dto.setStatus(null); // Service layer sets default status
        dto.setResolutionTime(null);
        dto.setResolutionMessage(null);

        AssistanceDTO createdRequest = assistanceRequestService.createRequest(dto);
        // It's good practice to return 201 Created for successful resource creation
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    /**
     * Endpoint for customers (to check their own) or admins (to view any)
     * to get a specific assistance request by its ID.
     * Maps to GET /api/assistance/{id}
     *
     * @param id The ID of the assistance request.
     * @return ResponseEntity with the AssistanceDTO if found, or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssistanceDTO> getRequestById(@PathVariable Long id) {
        AssistanceDTO request = assistanceRequestService.getRequestById(id);
        if (request == null) {
            // Return 404 Not Found if the request does not exist
            return ResponseEntity.notFound().build();
        }
        // Return 200 OK with the request details
        return ResponseEntity.ok(request);
    }

    /**
     * Endpoint for admins to retrieve all assistance requests.
     * This endpoint should typically be secured to only allow admin access.
     * Maps to GET /api/assistance
     *
     * @return ResponseEntity with a list of all AssistanceDTOs and HTTP 200 OK status.
     */
    @GetMapping
    public ResponseEntity<List<AssistanceDTO>> getAllRequests() {
        List<AssistanceDTO> requests = assistanceRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Endpoint for customers to retrieve their own assistance requests.
     * Maps to GET /api/assistance/user/{userId}
     *
     * @param userId The ID of the user whose requests to retrieve.
     * @return ResponseEntity with a list of AssistanceDTOs for the user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssistanceDTO>> getRequestsByUserId(@PathVariable Long userId) {
        List<AssistanceDTO> requests = assistanceRequestService.getRequestsByUserId(userId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Endpoint for an admin to resolve a specific assistance request.
     * This endpoint should be secured to only allow admin access.
     * Maps to PUT /api/assistance/{id}/resolve
     *
     * @param id The ID of the request to be resolved.
     * @param resolutionMessage The resolution message provided by the admin.
     * @return ResponseEntity with the updated AssistanceDTO if successful, or 404 Not Found.
     */
    @PutMapping("/{id}/resolve")
    public ResponseEntity<AssistanceDTO> resolveAssistanceRequest(@PathVariable Long id, @RequestBody String resolutionMessage) {
        try {
            AssistanceDTO resolvedRequest = assistanceRequestService.resolveRequest(id, resolutionMessage);
            return ResponseEntity.ok(resolvedRequest); // Return 200 OK
        } catch (RuntimeException e) { // Catch the exception from service if request not found
            // For a production system, use a specific custom exception (e.g., ResourceNotFoundException)
            // and an @ControllerAdvice to map it to HttpStatus.NOT_FOUND
            return ResponseEntity.notFound().build(); // Return 404 if request not found
        }
    }

    /**
     * Endpoint for an admin to delete a specific assistance request.
     * This endpoint should be secured to only allow admin access.
     * Maps to DELETE /api/assistance/{id}
     *
     * @param id The ID of the request to be deleted.
     * @return ResponseEntity with a success message and HTTP 200 OK status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long id) {
        // In a real application, you'd check if the request exists before trying to delete,
        // or rely on a custom exception from the service layer.
        assistanceRequestService.deleteRequest(id);
        return ResponseEntity.ok("Assistance request with ID " + id + " deleted successfully.");
    }
}