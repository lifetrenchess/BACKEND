package insurance.module.insurancemodule.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long bookingId;
    private Long userId;
    private Long packageId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
} 