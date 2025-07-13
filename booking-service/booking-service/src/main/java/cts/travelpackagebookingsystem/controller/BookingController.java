package cts.travelpackagebookingsystem.controller;

import java.util.List;

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

import cts.travelpackagebookingsystem.model.BookingDTO;
import cts.travelpackagebookingsystem.service.BookingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@PostMapping
	public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDTO bookingDto) {
		try {
			BookingDTO saved = bookingService.saveBooking(bookingDto);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to create booking: " + e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllBookings() {
		try {
			List<BookingDTO> bookings = bookingService.getAllBookings();
			return ResponseEntity.ok(bookings);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to fetch bookings: " + e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getBookingById(@PathVariable Long id) {
		try {
			BookingDTO booking = bookingService.getBookingById(id);
			return ResponseEntity.ok(booking);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingDTO bookingDTO) {
		try {
			BookingDTO updated = bookingService.updateBooking(id, bookingDTO);
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to update booking: " + e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
		try {
			bookingService.deleteBooking(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to delete booking: " + e.getMessage());
		}
	}
}
