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

import cts.travelpackagebookingsystem.enums.BookingStatus;
import cts.travelpackagebookingsystem.model.BookingDTO;
import cts.travelpackagebookingsystem.service.BookingService;



@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@PostMapping
	public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDto) {
	    BookingDTO saved = bookingService.saveBooking(bookingDto);
	    return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	
	@GetMapping
	public List<BookingDTO> getAllBookings(){
		return bookingService.getAllBookings();
	}
	
	@GetMapping("/{id}")
	public BookingDTO getBookingById(@PathVariable Long id) {
		return bookingService.getBookingById(id);
	}
	
	@PutMapping("/{id}")
	public BookingDTO updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
		return bookingService.updateBooking(id, bookingDTO);
	}
	
	@DeleteMapping("/{id}")
	public void deleteBooking(@PathVariable Long id) {
		 bookingService.deleteBooking(id);
	}
	
	

}
