package insurance.module.insurancemodule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Import for LocalDateTime

@Entity
@Table(name = "insurance_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceId;

    // These fields will be NULL for predefined packages (templates),
    // and populated for actual user selections.
    @Column(nullable = true) // Explicitly nullable for predefined packages
    private Long userId;
    @Column(nullable = true) // Explicitly nullable for predefined packages
    private Long bookingId;
    @Column(nullable = true) // To record when a user selection was made
    private LocalDateTime selectionDate;

    @Column(nullable = false)
    private String coverageDetails; // Details like "Basic coverage...", "Standard coverage..."

    @Column(nullable = false)
    private String provider; // e.g., "SafeGuard Insurance Co."

    @Column(nullable = false)
    private String status; // CRITICAL: Use "PREDEFINED_AVAILABLE" for templates, "PENDING", "PURCHASED" for selections

    @Column(nullable = false) // Package type is essential for both templates and selections
    private String packageType;

    @Column(nullable = false) // Price is essential for both templates and selections
    private int price;
}