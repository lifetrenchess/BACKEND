package insurance.module.insurancemodule.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Import for LocalDateTime

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceDTO {

    private Long insuranceId;
    // userId and bookingId can be null for predefined packages,
    // validation for selection will be handled in service layer.
    private Long userId;
    private Long bookingId;
    private LocalDateTime selectionDate; // Added for user selection timestamp

    @NotBlank(message = "Coverage details are required")
    private String coverageDetails;

    @NotBlank(message = "Provider name is required")
    private String provider;

    // Status is now crucial for distinguishing predefined packages from user selections
    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Package type is required")
    private String packageType;

    private int price;
}