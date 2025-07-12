package insurance.module.insurancemodule.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import insurance.module.insurancemodule.model.InsuranceDTO;
import insurance.module.insurancemodule.service.InsuranceService;
import jakarta.validation.Valid; // For @Valid on DTOs
import jakarta.validation.constraints.NotNull; // For @RequestParam validation

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    // --- User-Facing Endpoints ---

    // Endpoint for the frontend to get the list of AVAILABLE predefined insurance packages
    // Users will see these options to choose from.
    @GetMapping("/packages")
    public List<InsuranceDTO> getAvailablePredefinedPackages() {
        return insuranceService.getPredefinedPackages();
    }

    // Endpoint for a user to SELECT one of the predefined packages for a booking.
    // It expects the ID of the chosen predefined package, and the userId/bookingId from other modules.
    @PostMapping("/select")
    public ResponseEntity<InsuranceDTO> selectPredefinedPackage(
            @RequestParam @NotNull(message = "Predefined Package ID is required") Long predefinedPackageId, // The ID of the package chosen from /packages
            @RequestParam @NotNull(message = "User ID is required") Long userId,
            @RequestParam @NotNull(message = "Booking ID is required") Long bookingId) {
        try {
            InsuranceDTO newSelection = insuranceService.selectInsuranceForBooking(predefinedPackageId, userId, bookingId);
            return new ResponseEntity<>(newSelection, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Log the exception
            return ResponseEntity.badRequest().body(null); // Or return a custom ErrorDTO
        } catch (Exception e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // --- Admin/Internal/Reporting Endpoints (for managing templates or viewing selections) ---

    // Admin: Create a new predefined insurance package template.
    // This is for internal use, not for users to create custom insurance.
    // Ensure the DTO has 'PREDEFINED_AVAILABLE' status and null userId/bookingId
    @PostMapping("/admin/package")
    public ResponseEntity<InsuranceDTO> createPredefinedPackageTemplate(@Valid @RequestBody InsuranceDTO dto) {
        // Force status to be PREDEFINED_AVAILABLE for new templates
        dto.setStatus("PREDEFINED_AVAILABLE");
        dto.setUserId(null);    // Ensure no user/booking info for templates
        dto.setBookingId(null);
        dto.setSelectionDate(null);
        return new ResponseEntity<>(insuranceService.createInsurance(dto), HttpStatus.CREATED);
    }

    // Admin: Update any insurance record (predefined package template or a user's selection).
    // Be careful with what fields can be updated depending on status.
    @PutMapping("/{id}")
    public InsuranceDTO updateInsuranceRecord(@PathVariable Long id, @Valid @RequestBody InsuranceDTO dto) {
        return insuranceService.updateInsurance(id, dto);
    }

    // Admin: Delete any insurance record (use with caution, especially for templates)
    @DeleteMapping("/{id}")
    public void deleteInsuranceRecord(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
    }

    // Admin/Reporting: Get a specific insurance record by its ID (template or selection)
    @GetMapping("/{id}")
    public InsuranceDTO getInsuranceRecordById(@PathVariable Long id) {
        return insuranceService.getInsuranceById(id);
    }

    // Admin/Reporting: Get ALL insurance records (predefined templates + all user selections)
    @GetMapping // Renamed from getAll to a clearer path
    public List<InsuranceDTO> getAllInsuranceRecords() {
        return insuranceService.getAllInsurance();
    }

    // Reporting: Get all insurance selections for a specific booking (user-made selections only)
    @GetMapping("/selections/booking/{bookingId}")
    public List<InsuranceDTO> getInsuranceSelectionsByBookingId(@PathVariable Long bookingId) {
        return insuranceService.getInsuranceSelectionsByBookingId(bookingId);
    }

    // Reporting: Get all insurance selections made by a specific user (user-made selections only)
    @GetMapping("/selections/user/{userId}")
    public List<InsuranceDTO> getInsuranceSelectionsByUserId(@PathVariable Long userId) {
        return insuranceService.getInsuranceSelectionsByUserId(userId);
    }
}