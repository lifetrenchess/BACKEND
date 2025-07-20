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
	
	@NotNull(message = "Booking ID is required")
	private Long bookingId;
	
	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
	private Double amount;
	
	private String status;
	
	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
	
	@NotBlank(message = "Currency is required")
	private String currency;
}
