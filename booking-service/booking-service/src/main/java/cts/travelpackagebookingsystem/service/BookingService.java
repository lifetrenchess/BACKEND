package cts.travelpackagebookingsystem.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cts.travelpackagebookingsystem.entity.Booking;
import cts.travelpackagebookingsystem.enums.BookingStatus;
import cts.travelpackagebookingsystem.exception.ResourceNotFoundException;
import cts.travelpackagebookingsystem.model.BookingDTO;
import cts.travelpackagebookingsystem.repository.BookingRepository;
@Service
public class BookingService {
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Transactional
	public BookingDTO saveBooking(BookingDTO bookingDTO) {
		Booking booking = modelMapper.map(bookingDTO, Booking.class);
		booking.setStatus(BookingStatus.CONFIRMED);
		Booking savedBooking = bookingRepo.save(booking);
		return modelMapper.map(savedBooking,BookingDTO.class);
	}
	
	public List<BookingDTO> getAllBookings(){
		return bookingRepo.findAll().stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .toList();
		}
		
		
	
	
	public BookingDTO getBookingById(Long id) {
		Booking booking = bookingRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
		return modelMapper.map(booking,  BookingDTO.class);
	}
	
	public BookingDTO updateBooking(Long id, BookingDTO updatedBookingDTO) {
		Booking booking = bookingRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
		
			booking.setPackageId(updatedBookingDTO.getPackageId());
			booking.setUserId(updatedBookingDTO.getUserId());
			booking.setStartDate(updatedBookingDTO.getStartDate());
			booking.setEndDate(updatedBookingDTO.getEndDate());
			booking.setStatus(updatedBookingDTO.getStatus());
			
			Booking updatedBooking = bookingRepo.save(booking);
			return modelMapper.map(updatedBooking, BookingDTO.class);
			
		
		
	}

	public void deleteBooking(Long id) {
		bookingRepo.deleteById(id);
	}
	
	
}
