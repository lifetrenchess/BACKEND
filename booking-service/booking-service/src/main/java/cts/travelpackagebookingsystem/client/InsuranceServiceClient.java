package cts.travelpackagebookingsystem.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "INSURANCE-SERVICE")
public interface InsuranceServiceClient {

    @PostMapping("/api/insurance/select")
    InsuranceDTO selectInsuranceForBooking(
        @RequestParam("predefinedPackageId") Long predefinedPackageId,
        @RequestParam("userId") Long userId,
        @RequestParam("bookingId") Long bookingId
    );
} 