package cts.travelpackagebookingsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import cts.travelpackagebookingsystem.enums.BookingStatus;
import cts.travelpackagebookingsystem.enums.PaymentStatus;
import cts.travelpackagebookingsystem.entity.Booking;
import cts.travelpackagebookingsystem.entity.Payment;
import cts.travelpackagebookingsystem.exception.ResourceNotFoundException;
import cts.travelpackagebookingsystem.model.PaymentDTO;
import cts.travelpackagebookingsystem.model.OrderResponse;
import cts.travelpackagebookingsystem.repository.BookingRepository;
import cts.travelpackagebookingsystem.repository.PaymentRepository;

@Service
public class PaymentService {
	@Autowired
	private PaymentRepository paymentRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private RazorpayClient razorpayClient;
	
	@Value("${razorpay.key.id}")
	private String razorpayKeyId;

	@Value("${razorpay.key.secret}")
	private String razorpayKeySecret;

	// Create Razorpay Order
	public OrderResponse createRazorpayOrder(PaymentDTO paymentDTO) throws RazorpayException {
		try {
			// Validate booking exists
			Booking booking = bookingRepo.findById(paymentDTO.getBookingId())
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + paymentDTO.getBookingId()));
			
			// Create JSON object for Razorpay order
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", paymentDTO.getAmount() * 100); // Convert to paisa
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "booking_" + paymentDTO.getBookingId());
			
			// Create order with Razorpay
			com.razorpay.Order order = razorpayClient.orders.create(orderRequest);
			
			// Create payment record in database
			Payment payment = new Payment();
			payment.setUserId(paymentDTO.getUserId());
			payment.setAmount(paymentDTO.getAmount());
			payment.setStatus(PaymentStatus.PENDING);
			payment.setPaymentMethod(paymentDTO.getPaymentMethod());
			payment.setRazorpayOrderId(order.get("id").toString());
			payment.setCurrency("INR");
			payment.setBooking(booking);
			
			// Save payment
			paymentRepo.save(payment);
			
			// Return order response
			return new OrderResponse(
				order.get("id").toString(),
				"INR",
				paymentDTO.getAmount(),
				razorpayKeyId
			);
			
		} catch (RazorpayException e) {
			throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
		}
	}

	// Verify Razorpay Payment
	@Transactional
	public String verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
		try {
			JSONObject attributesForVerification = new JSONObject();
			attributesForVerification.put("razorpay_order_id", razorpayOrderId);
			attributesForVerification.put("razorpay_payment_id", razorpayPaymentId);
			attributesForVerification.put("razorpay_signature", razorpaySignature);

			boolean isValid = Utils.verifyPaymentSignature(attributesForVerification, razorpayKeySecret);

			if (isValid) {
				updatePaymentAndBookingStatus(razorpayOrderId, razorpayPaymentId, PaymentStatus.COMPLETED);
				return "Payment verified successfully.";
			} else {
				updatePaymentAndBookingStatus(razorpayOrderId, razorpayPaymentId, PaymentStatus.FAILED);
				return "Payment verification failed.";
			}

		} catch (Exception e) {
			throw new RuntimeException("Error verifying payment: " + e.getMessage(), e);
		}
	}

	// Update payment and booking status
	@Transactional
	public void updatePaymentAndBookingStatus(String razorpayOrderId, String razorpayPaymentId, PaymentStatus status) {
		Optional<Payment> optionalPayment = paymentRepo.findByRazorpayOrderId(razorpayOrderId);

		if (optionalPayment.isEmpty()) {
			throw new ResourceNotFoundException("Payment not found for Order ID: " + razorpayOrderId);
		}

		Payment payment = optionalPayment.get();
		payment.setRazorpayPaymentId(razorpayPaymentId);
		payment.setStatus(status);

		Payment updatedPayment = paymentRepo.save(payment);

		// Update booking status
		Booking associatedBooking = updatedPayment.getBooking();
		if (associatedBooking != null) {
			if (status == PaymentStatus.COMPLETED) {
				associatedBooking.setStatus(BookingStatus.CONFIRMED);
				associatedBooking.setPayment(payment);
			} else {
				associatedBooking.setStatus(BookingStatus.PENDING);
			}
			bookingRepo.save(associatedBooking);
		}
	}

	// Mock payment method (keep as alternative)
	public PaymentDTO createMockPayment(PaymentDTO paymentDTO) {
		Booking booking = bookingRepo.findById(paymentDTO.getBookingId())
			.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + paymentDTO.getBookingId()));
		
		if (paymentRepo.existsByBooking(booking)) {
			throw new IllegalStateException("A payment already exists for this booking.");
		}

		Payment payment = modelMapper.map(paymentDTO, Payment.class);
		payment.setStatus(PaymentStatus.COMPLETED);
		payment.setBooking(booking);
		booking.setPayment(payment);
		booking.setStatus(BookingStatus.CONFIRMED);

		Payment savedPayment = paymentRepo.save(payment);
		bookingRepo.save(booking);
	   
		return modelMapper.map(savedPayment, PaymentDTO.class);
	}

	// Get all payments
	public List<PaymentDTO> getAllPayment() {
		List<Payment> payments = paymentRepo.findAll();
		List<PaymentDTO> paymentDTOs = new ArrayList<>();
		for(Payment payment : payments) {
			paymentDTOs.add(modelMapper.map(payment, PaymentDTO.class));
		}
		return paymentDTOs;
	}
	
	// Get payment by ID
	public PaymentDTO getPaymentById(Long id) {
		Optional<Payment> paymentOpt = paymentRepo.findById(id);
		if(paymentOpt.isPresent()) {
			Payment payment = paymentOpt.get();
			return modelMapper.map(payment, PaymentDTO.class);
		} else {
			throw new ResourceNotFoundException("Payment not found with ID: " + id);
		}
	}
	
	// Update payment
	public PaymentDTO updatePayment(Long id, PaymentDTO updatedPaymentDTO) {
		Payment payment = paymentRepo.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));

		payment.setUserId(updatedPaymentDTO.getUserId());
		payment.setAmount(updatedPaymentDTO.getAmount());
		payment.setStatus(PaymentStatus.valueOf(updatedPaymentDTO.getStatus()));
		payment.setPaymentMethod(updatedPaymentDTO.getPaymentMethod());
			
		Payment updated = paymentRepo.save(payment);
		return modelMapper.map(updated, PaymentDTO.class);
	}
	
	// Delete payment
	public void deletePayment(Long id) {
		if (!paymentRepo.existsById(id)) {
			throw new ResourceNotFoundException("Cannot delete. Payment not found with ID: " + id);
		}
		paymentRepo.deleteById(id);
	}
}