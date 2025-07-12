package insurance.module.insurancemodule.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingServiceClient {

    @GetMapping("/api/bookings/{bookingId}")
    BookingDTO getBookingById(@PathVariable("bookingId") Long bookingId);
} 