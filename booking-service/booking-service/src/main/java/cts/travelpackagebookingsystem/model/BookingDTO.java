package cts.travelpackagebookingsystem.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import cts.travelpackagebookingsystem.entity.Payment;
import cts.travelpackagebookingsystem.enums.BookingStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
	private Long bookingId;
	
	@NotNull(message = "User ID is required")
	private Long userId;
	
	@NotNull(message = "Package ID is required")
	private Long packageId;
	
	@NotNull(message = "Start date is required")
	@FutureOrPresent(message = "Start date must be today or in the future")
	private LocalDate startDate;
	
	@NotNull(message = "End date is required")
	@FutureOrPresent(message = "End date must be in the future")
	private LocalDate endDate;
	
	@NotNull(message = "Status is required")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BookingStatus status;
	
	// Traveler information
	private Integer adults;
	private Integer children;
	private Integer infants;
	private String contactFullName;
	private String contactEmail;
	private String contactPhone;
	private String travelerNames;
	private Boolean hasInsurance;
	private String insurancePlan;
	
	private Payment payment;
}
