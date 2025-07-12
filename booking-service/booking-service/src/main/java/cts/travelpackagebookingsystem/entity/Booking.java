package cts.travelpackagebookingsystem.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cts.travelpackagebookingsystem.enums.BookingStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;
	
	private Long userId;
	private Long packageId;
	private LocalDate startDate;
	private LocalDate endDate;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status;
	
	@Column(name = "razorpay_payment_id")
	private String razorpayPaymentId;
	
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
	
	@OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
	@JsonIgnore
	private Payment payment;
    
	
	

}
