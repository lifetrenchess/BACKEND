package cts.travelpackagebookingsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject; // Keep this if other methods in this service need it
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import cts.travelpackagebookingsystem.enums.BookingStatus; // Make sure these enums exist
import cts.travelpackagebookingsystem.enums.PaymentStatus; // Make sure these enums exist
import cts.travelpackagebookingsystem.entity.Booking;
import cts.travelpackagebookingsystem.entity.Payment;
import cts.travelpackagebookingsystem.exception.ResourceNotFoundException;
import cts.travelpackagebookingsystem.model.PaymentDTO;
import cts.travelpackagebookingsystem.model.OrderResponse;
import cts.travelpackagebookingsystem.repository.BookingRepository;
import cts.travelpackagebookingsystem.repository.PaymentRepository;

// NOTE: This PaymentService now contains Razorpay verification logic directly.
// If you also have a separate RazorPaymentService for order creation,
// ensure no duplicate logic or manage the responsibilities clearly.
// For now, I'm integrating the verification and status update methods you provided into this service.
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

	@Value("${razorpay.currency}")
	private String razorpayCurrency;

	// Create Razorpay Order
	public OrderResponse createRazorpayOrder(PaymentDTO paymentDTO) throws RazorpayException {
		try {
			// Validate booking exists
			Booking booking = bookingRepo.findById(paymentDTO.getBookingId())
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + paymentDTO.getBookingId()));
			
			// Create JSON object for Razorpay order
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", paymentDTO.getAmount() * 100); // Convert to paisa
			orderRequest.put("currency", paymentDTO.getCurrency());
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
			payment.setCurrency(paymentDTO.getCurrency());
			payment.setBooking(booking);
			
			// Save payment
			Payment savedPayment = paymentRepo.save(payment);
			
			// Return order response
			return new OrderResponse(
				order.get("id").toString(),
				paymentDTO.getCurrency(),
				paymentDTO.getAmount(),
				razorpayKeyId
			);
			
		} catch (RazorpayException e) {
			throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
		}
	}

	@Transactional
	    public String verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
	        try {
	            JSONObject attributesForVerification = new JSONObject();
	            attributesForVerification.put("razorpay_order_id", razorpayOrderId);
	            attributesForVerification.put("razorpay_payment_id", razorpayPaymentId);
	            attributesForVerification.put("razorpay_signature", razorpaySignature);

	            // Check if razorpayKeySecret is null or empty before using it
	            if (razorpayKeySecret == null || razorpayKeySecret.isEmpty()) {
	                throw new RazorpayException("Razorpay Key Secret is not configured.");
	            }

	            boolean isValid = Utils.verifyPaymentSignature(attributesForVerification, razorpayKeySecret);

	            String statusToUpdate = isValid ? PaymentStatus.COMPLETED.name() : PaymentStatus.FAILED.name();

	            // Call the method to update payment and booking status based on verification result
	            updatePaymentAndBookingStatus(razorpayOrderId, razorpayPaymentId, statusToUpdate);

	            return isValid ? "Payment verified successfully." : "Payment verification failed.";

	        } catch (RazorpayException e) {
	            // Log the exception for debugging
	            System.err.println("Error verifying Razorpay payment signature: " + e.getMessage());
	            e.printStackTrace();
	            throw new RuntimeException("Error verifying Razorpay payment: " + e.getMessage(), e);
	        }
	    }

	    @Transactional
	    public PaymentDTO updatePaymentAndBookingStatus(String razorpayOrderId, String razorpayPaymentId, String newStatus) {
	        // Find the payment record using the Razorpay Order ID
	        // Ensure PaymentRepository has findByRazorpayOrderId method
	        Optional<Payment> optionalPayment = paymentRepo.findByRazorpayOrderId(razorpayOrderId);

	        if (optionalPayment.isEmpty()) {
	            // Use your existing ResourceNotFoundException
	            throw new ResourceNotFoundException("Payment not found for Razorpay Order ID: " + razorpayOrderId);
	        }

	        Payment payment = optionalPayment.get();
	        payment.setRazorpayPaymentId(razorpayPaymentId);
	        // FIX: Convert String newStatus to PaymentStatus enum
	        payment.setStatus(PaymentStatus.valueOf(newStatus));

	        Payment updatedPayment = paymentRepo.save(payment);

	        // Assuming Payment entity has a direct reference to Booking entity named 'booking'
	        Booking associatedBooking = updatedPayment.getBooking();
	        
	        if (associatedBooking != null) {
	            // Update the booking status based on payment result
	            if (PaymentStatus.COMPLETED.name().equalsIgnoreCase(newStatus)) {
	                associatedBooking.setStatus(BookingStatus.CONFIRMED);
	                associatedBooking.setPayment(payment);
	                System.out.println("Payment " + razorpayPaymentId + " for Order " + razorpayOrderId + " completed. Booking " + associatedBooking.getBookingId() + " confirmed.");
	            } else if (PaymentStatus.FAILED.name().equalsIgnoreCase(newStatus)) {
	                associatedBooking.setStatus(BookingStatus.PENDING);
	                associatedBooking.setPayment(null);
	                System.out.println("Payment " + razorpayPaymentId + " for Order " + razorpayOrderId + " failed. Booking " + associatedBooking.getBookingId() + " status updated to PENDING.");
	            }
	            bookingRepo.save(associatedBooking);
	        } else {
	            System.err.println("Warning: Payment " + razorpayOrderId + " has no associated Booking entity.");
	        }

	        return modelMapper.map(updatedPayment, PaymentDTO.class);
	    }
	
	
	
	public PaymentDTO createPayment(PaymentDTO paymentDTO) {
	    
	    Booking booking = bookingRepo.findById(paymentDTO.getBookingId())
	        .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + paymentDTO.getBookingId()));
	    
	    if (paymentRepo.existsByBooking(booking)) {
	        throw new IllegalStateException("A payment already exists for this booking.");
	    }

	    
	    Payment payment = modelMapper.map(paymentDTO, Payment.class);

	    // FIX: Set status using enum literal
	    payment.setStatus(PaymentStatus.COMPLETED); 
	    payment.setBooking(booking);
	    booking.setPayment(payment);

	    
	    
	    Payment savedPayment = paymentRepo.save(payment);
	    bookingRepo.save(booking);
	   
	    return modelMapper.map(savedPayment, PaymentDTO.class);
	}


	
	public List<PaymentDTO> getAllPayment(){
		List<Payment> payments = paymentRepo.findAll();
		List<PaymentDTO> paymentDTOs = new ArrayList<>();
		for(Payment payment : payments) {
			paymentDTOs.add(modelMapper.map(payment, PaymentDTO.class));
		}
		return paymentDTOs;
	}
	
	public PaymentDTO getPaymentById(Long id) {
		Optional<Payment> paymentOpt = paymentRepo.findById(id);
		if(paymentOpt.isPresent()) {
			Payment payment = paymentOpt.get();
			return modelMapper.map(payment, PaymentDTO.class);
		}
		else {
			throw new ResourceNotFoundException("Payment not found with ID: " + id);
		}
		
	}
	
	public PaymentDTO updatePayment(Long id, PaymentDTO updatedPaymentDTO) {
		Payment payment = paymentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));

			
			payment.setUserId(updatedPaymentDTO.getUserId());
			payment.setAmount(updatedPaymentDTO.getAmount());
			// FIX: Convert String from DTO to enum
			payment.setStatus(PaymentStatus.valueOf(updatedPaymentDTO.getStatus())); 
			payment.setPaymentMethod(updatedPaymentDTO.getPaymentMethod());
			
			// These fields might not be present in your PaymentDTO but were mentioned in a previous version
			// If they are not, remove these lines or add them to your PaymentDTO
			// payment.setRazorpayOrderId(updatedPaymentDTO.getRazorpayOrderId()); 
			// payment.setRazorpayPaymentId(updatedPaymentDTO.getRazorpayPaymentId());
			// payment.setCurrency(updatedPaymentDTO.getCurrency());
			// payment.setBookingId(updatedPaymentDTO.getBookingId()); // This should be handled via the Booking object if it's a relationship
			
			Payment updated = paymentRepo.save(payment);
			return modelMapper.map(updated, PaymentDTO.class);
		
	}
	
	public void deletePayment(Long id) {
		if (!paymentRepo.existsById(id)) { // Added existence check for delete
            throw new ResourceNotFoundException("Cannot delete. Payment not found with ID: " + id);
        }
		paymentRepo.deleteById(id);
	}

}