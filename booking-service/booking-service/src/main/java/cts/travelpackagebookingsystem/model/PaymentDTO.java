package cts.travelpackagebookingsystem.model;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	private Long paymentId;
	
	@NotNull(message = "User ID is required")
	private Long userId;
	
	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
	private Double amount;
	
//	@NotBlank(message = "Status is required")
	private String status;
	
	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
	
	
	private Long bookingId;

}
