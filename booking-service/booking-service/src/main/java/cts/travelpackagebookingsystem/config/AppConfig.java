package cts.travelpackagebookingsystem.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies; // Import for STRICT strategy
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Import your entity and DTO classes
import cts.travelpackagebookingsystem.entity.Payment;
import cts.travelpackagebookingsystem.model.PaymentDTO;
import cts.travelpackagebookingsystem.entity.Booking; // Needed because Payment has a 'Booking' field

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // 1. Set the matching strategy to STRICT.
        // This is crucial to prevent ModelMapper from being too "smart" and trying to match
        // unintended nested properties to your DTO fields.
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 2. Define the TypeMap for Payment to PaymentDTO
        // This TypeMap allows us to explicitly define mappings.
        modelMapper.createTypeMap(Payment.class, PaymentDTO.class)
            // Explicitly tell ModelMapper how to get the bookingId for PaymentDTO
            // It comes from the nested Booking object: payment.getBooking().getBookingId()
            .addMapping(src -> src.getBooking().getBookingId(), PaymentDTO::setBookingId);

        // No need for the `addMappings(mapper -> { mapper.skip(...) })` block here.
        // With STRICT matching, and the explicit addMapping for bookingId,
        // ModelMapper should not try to use getBooking().getUserId() or getBooking().getPackageId()
        // for `setBookingId()` because their names and types don't exactly match `bookingId`.

        return modelMapper;
    }
}