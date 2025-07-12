package insurance.module.insurancemodule.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import insurance.module.insurancemodule.entity.Insurance;
import insurance.module.insurancemodule.exception.InsuranceIdIsNotFoundException;
import insurance.module.insurancemodule.model.InsuranceDTO;
import insurance.module.insurancemodule.repository.InsuranceRepository;

import java.time.LocalDateTime; // Import for LocalDateTime
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceService {

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private ModelMapper mapper;

    // Admin/Internal method: Use this to create new predefined packages or for general CRUD
    // For predefined packages, DTO should have status "PREDEFINED_AVAILABLE" and null userId/bookingId/selectionDate
    // For manual creation of a user's selection, status would be "PENDING", "PURCHASED" etc.
    public InsuranceDTO createInsurance(InsuranceDTO dto) {
        Insurance insurance = mapper.map(dto, Insurance.class);
        // Ensure that if a template, userId, bookingId, selectionDate are null
        // (This will be handled better by the DataInitializer and select method)
        if ("PREDEFINED_AVAILABLE".equals(insurance.getStatus())) {
            insurance.setUserId(null);
            insurance.setBookingId(null);
            insurance.setSelectionDate(null);
        }
        return mapper.map(insuranceRepository.save(insurance), InsuranceDTO.class);
    }

    // Public-facing method: Get all currently available predefined packages for user display
    public List<InsuranceDTO> getPredefinedPackages() {
        // Fetch records from DB where status is "PREDEFINED_AVAILABLE"
        return insuranceRepository.findByStatus("PREDEFINED_AVAILABLE").stream()
                .map(i -> mapper.map(i, InsuranceDTO.class))
                .collect(Collectors.toList());
    }

    // Core User Action: Creates a NEW insurance record representing a user's selection
    public InsuranceDTO selectInsuranceForBooking(Long predefinedPackageId, Long userId, Long bookingId) {
        // 1. Validate incoming IDs
        if (userId == null || bookingId == null || predefinedPackageId == null) {
            throw new IllegalArgumentException("User ID, Booking ID, and the ID of the selected package are required.");
        }

        // 2. Fetch the details of the chosen predefined package template
        Insurance chosenPackageTemplate = insuranceRepository.findById(predefinedPackageId)
                .orElseThrow(() -> new InsuranceIdIsNotFoundException("Predefined Insurance Package with ID " + predefinedPackageId + " not found."));

        // Ensure the found package is indeed a predefined one (and not already a selection)
        if (!"PREDEFINED_AVAILABLE".equals(chosenPackageTemplate.getStatus())) {
            throw new IllegalArgumentException("The selected insurance ID is not an active predefined package. Status: " + chosenPackageTemplate.getStatus());
        }

        // 3. Create a NEW Insurance entity instance for this specific user's selection
        //    Copying details from the predefined template
        Insurance newSelection = Insurance.builder()
                .userId(userId)
                .bookingId(bookingId)
                .coverageDetails(chosenPackageTemplate.getCoverageDetails())
                .provider(chosenPackageTemplate.getProvider())
                .status("PENDING") // Set initial status for this new selection (e.g., awaiting payment)
                .packageType(chosenPackageTemplate.getPackageType())
                .price(chosenPackageTemplate.getPrice()) // Capture the price at the time of selection
                .selectionDate(LocalDateTime.now()) // Record the timestamp of selection
                .build();

        // 4. Save this new selection record to the database
        return mapper.map(insuranceRepository.save(newSelection), InsuranceDTO.class);
    }

    // Admin/Internal: Get a specific insurance record (can be predefined or selection)
    public InsuranceDTO getInsuranceById(Long id) {
        Insurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new InsuranceIdIsNotFoundException("Insurance ID " + id + " not found"));
        return mapper.map(insurance, InsuranceDTO.class);
    }

    // Admin/Internal: Update an existing insurance record (predefined or selection)
    public InsuranceDTO updateInsurance(Long id, InsuranceDTO dto) {
        Insurance existing = insuranceRepository.findById(id)
                .orElseThrow(() -> new InsuranceIdIsNotFoundException("Insurance ID not found"));

        // Update logic: Use ModelMapper or manually set fields.
        // Be careful with userId, bookingId, selectionDate for PREDEFINED_AVAILABLE packages
        // if these fields are included in the DTO from the update request.
        existing.setCoverageDetails(dto.getCoverageDetails());
        existing.setProvider(dto.getProvider());
        existing.setStatus(dto.getStatus());
        existing.setPackageType(dto.getPackageType());
        existing.setPrice(dto.getPrice());

        // Only update selection-specific fields if they are provided and applicable
        // This prevents overwriting nulls on predefined packages with invalid data
        if (dto.getUserId() != null) existing.setUserId(dto.getUserId());
        if (dto.getBookingId() != null) existing.setBookingId(dto.getBookingId());
        if (dto.getSelectionDate() != null) existing.setSelectionDate(dto.getSelectionDate());


        return mapper.map(insuranceRepository.save(existing), InsuranceDTO.class);
    }

    // Admin/Internal: Delete an insurance record
    public void deleteInsurance(Long id) {
        insuranceRepository.deleteById(id);
    }

    // Admin/Reporting: Get all insurance records (both predefined and selections)
    public List<InsuranceDTO> getAllInsurance() { // Renamed from getAll to getAllInsurance for clarity
        return insuranceRepository.findAll().stream()
                .map(i -> mapper.map(i, InsuranceDTO.class))
                .collect(Collectors.toList());
    }

    // Reporting: Get a user's specific insurance selections for a booking
    public List<InsuranceDTO> getInsuranceSelectionsByBookingId(Long bookingId) {
        // Exclude predefined templates when retrieving actual user selections
        return insuranceRepository.findByBookingIdAndStatusNot(bookingId, "PREDEFINED_AVAILABLE").stream()
                .map(s -> mapper.map(s, InsuranceDTO.class))
                .collect(Collectors.toList());
    }

    // Reporting: Get all insurance selections made by a specific user
    public List<InsuranceDTO> getInsuranceSelectionsByUserId(Long userId) {
        // Exclude predefined templates
        return insuranceRepository.findByUserIdAndStatusNot(userId, "PREDEFINED_AVAILABLE").stream()
                .map(s -> mapper.map(s, InsuranceDTO.class))
                .collect(Collectors.toList());
    }
}