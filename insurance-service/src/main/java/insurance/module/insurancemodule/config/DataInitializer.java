package insurance.module.insurancemodule.config;

import insurance.module.insurancemodule.entity.Insurance;
import insurance.module.insurancemodule.repository.InsuranceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final InsuranceRepository insuranceRepository;

    public DataInitializer(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if predefined packages already exist to prevent duplicates on every restart
        // We'll check for any records with the "PREDEFINED_AVAILABLE" status
        if (insuranceRepository.findByStatus("PREDEFINED_AVAILABLE").isEmpty()) {
            System.out.println("Initializing predefined insurance packages...");
            // Create and save your predefined insurance packages
            Insurance smallPackage = Insurance.builder()
                    .packageType("Small")
                    .coverageDetails("Basic coverage for personal accidents and theft (Medical expenses up to Rs 50,000, Lost luggage up to Rs 5,000).")
                    .provider("SafeGuard Insurance Co.")
                    .status("PREDEFINED_AVAILABLE") // Unique status for templates
                    .price(599)
                    .userId(null)    // Templates don't have user/booking info
                    .bookingId(null)
                    .selectionDate(null)
                    .build();

            Insurance mediumPackage = Insurance.builder()
                    .packageType("Medium")
                    .coverageDetails("Standard coverage including accidents, theft, and fire (Medical expenses up to Rs 1,00,000, Lost luggage up to Rs 10,000, Trip cancellation up to Rs 20,000).")
                    .provider("ShieldsSecure Insurance Ltd.")
                    .status("PREDEFINED_AVAILABLE")
                    .price(899)
                    .userId(null)
                    .bookingId(null)
                    .selectionDate(null)
                    .build();

            Insurance largePackage = Insurance.builder()
                    .packageType("Large")
                    .coverageDetails("Premium coverage with full protection and roadside assistance (Medical expenses up to Rs 2,00,000, Lost luggage up to Rs 20,000, Trip cancellation up to Rs 50,000, Emergency evacuation).")
                    .provider("TitanCover Assurance Group")
                    .status("PREDEFINED_AVAILABLE")
                    .price(1000)
                    .userId(null)
                    .bookingId(null)
                    .selectionDate(null)
                    .build();

            insuranceRepository.saveAll(List.of(smallPackage, mediumPackage, largePackage)); // Save all at once

            System.out.println("Predefined insurance packages initialized in the database.");
        } else {
            System.out.println("Predefined insurance packages already exist. Skipping initialization.");
        }
    }
}