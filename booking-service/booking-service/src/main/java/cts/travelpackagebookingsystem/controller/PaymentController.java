package cts.travelpackagebookingsystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cts.travelpackagebookingsystem.model.PaymentDTO;
import cts.travelpackagebookingsystem.model.OrderResponse;
import cts.travelpackagebookingsystem.service.PaymentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	// Create Razorpay order
	@PostMapping
	public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
		try {
			OrderResponse orderResponse = paymentService.createRazorpayOrder(paymentDTO);
			return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to create payment order: " + e.getMessage());
		}
	}

	// Verify Razorpay payment
	@PostMapping("/verify")
	public ResponseEntity<String> verifyPayment(@RequestBody Map<String, Object> data) {
		String razorpayOrderId = (String) data.get("razorpay_order_id");
		String razorpayPaymentId = (String) data.get("razorpay_payment_id");
		String razorpaySignature = (String) data.get("razorpay_signature");

		if (razorpayOrderId == null || razorpayPaymentId == null || razorpaySignature == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Missing required Razorpay parameters for verification.");
		}

		try {
			String result = paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error verifying payment: " + e.getMessage());
		}
	}

	// Mock payment endpoint (alternative)
	@PostMapping("/mock")
	public ResponseEntity<?> createMockPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
		try {
			PaymentDTO payment = paymentService.createMockPayment(paymentDTO);
			return new ResponseEntity<>(payment, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to create mock payment: " + e.getMessage());
		}
	}
	
	// Get all payments
	@GetMapping
	public ResponseEntity<?> getAllPayment() {
		try {
			List<PaymentDTO> payments = paymentService.getAllPayment();
			return ResponseEntity.ok(payments);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to fetch payments: " + e.getMessage());
		}
	}
	
	// Get payment by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
		try {
			PaymentDTO payment = paymentService.getPaymentById(id);
			return ResponseEntity.ok(payment);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	// Update payment
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentDTO paymentDTO) {
		try {
			PaymentDTO updated = paymentService.updatePayment(id, paymentDTO);
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to update payment: " + e.getMessage());
		}
	}
	
	// Delete payment
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePayment(@PathVariable Long id) {
		try {
			paymentService.deletePayment(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to delete payment: " + e.getMessage());
		}
	}
}
