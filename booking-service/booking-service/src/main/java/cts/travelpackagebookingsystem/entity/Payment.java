package cts.travelpackagebookingsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cts.travelpackagebookingsystem.enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	
	private Long userId;
	private Double amount;
	@Enumerated(EnumType.STRING)
    private PaymentStatus status;
	private String paymentMethod;
	
	

    @Column(name = "razorpay_order_id", unique = true) // Ensures uniqueness of Razorpay Order ID [cite: 8]
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id") // Not unique, as a single order can have multiple payment attempts/IDs 
    private String razorpayPaymentId;

    @Column(name = "currency")
    private String currency; 

	
	
	@OneToOne
	@JoinColumn(name = "booking_id", referencedColumnName = "bookingId" )
	@JsonIgnore
	private Booking booking;
	

}
