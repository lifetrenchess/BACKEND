package cts.travelpackagebookingsystem.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceDTO {
    private Long insuranceId;
    private Long userId;
    private Long bookingId;
    private LocalDateTime selectionDate;
    private String coverageDetails;
    private String provider;
    private String status;
    private String packageType;
    private Integer price;
} 