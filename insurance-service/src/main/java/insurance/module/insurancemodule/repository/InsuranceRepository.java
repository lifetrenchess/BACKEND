package insurance.module.insurancemodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Import missing annotation

import insurance.module.insurancemodule.entity.Insurance;

import java.util.List;

@Repository // Added missing @Repository annotation
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    // Find all insurance records that are marked as predefined and available
    List<Insurance> findByStatus(String status);

    // Find all user selections for a given booking ID (excluding the predefined templates)
    List<Insurance> findByBookingIdAndStatusNot(Long bookingId, String excludedStatus);

    // Find all user selections for a given user ID (excluding the predefined templates)
    List<Insurance> findByUserIdAndStatusNot(Long userId, String excludedStatus);
}