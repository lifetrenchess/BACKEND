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
import cts.travelpackagebookingsystem.service.PaymentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/verifyPayment")
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
	
	
	
	@PostMapping
	public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
		PaymentDTO saved = paymentService.createPayment(paymentDTO);
		return new ResponseEntity<>(saved,HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<PaymentDTO> getAllPayment(){
		return paymentService.getAllPayment();
	}
	
	@GetMapping("/{id}")
	public PaymentDTO getPaymentById(@PathVariable Long id) {
		return paymentService.getPaymentById(id);
	}
	
	@PutMapping("/{id}")
	public PaymentDTO updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
		return paymentService.updatePayment(id, paymentDTO);
	}
	
	@DeleteMapping("/{id}")
	public void deletePayment(@PathVariable Long id) {
		paymentService.deletePayment(id);
	}
	

}
