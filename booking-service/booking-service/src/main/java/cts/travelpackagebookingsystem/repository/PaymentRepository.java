package cts.travelpackagebookingsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cts.travelpackagebookingsystem.entity.Booking;
import cts.travelpackagebookingsystem.entity.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	boolean existsByBooking(Booking booking);
	 Optional<Payment> findByRazorpayOrderId(String razorpayOrderId); // 
	 Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId); // New method to find by Razorpay's actual payment ID

}
