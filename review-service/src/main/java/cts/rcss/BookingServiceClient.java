package cts.rcss;

import cts.rcss.model.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingServiceClient {

    @GetMapping("/api/bookings/user/{userId}")
    List<BookingDTO> getBookingsByUserId(@PathVariable("userId") Long userId);
    
    @GetMapping("/api/bookings/user/{userId}/package/{packageId}")
    BookingDTO getBookingByUserIdAndPackageId(@PathVariable("userId") Long userId, @PathVariable("packageId") Long packageId);
} 