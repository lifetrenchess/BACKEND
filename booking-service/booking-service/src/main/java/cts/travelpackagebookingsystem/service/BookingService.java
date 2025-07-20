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
import cts.travelpackagebookingsystem.client.UserServiceClient;
import cts.travelpackagebookingsystem.client.PackageServiceClient;
import cts.travelpackagebookingsystem.client.UserDTO;
import cts.travelpackagebookingsystem.client.PackageDTO;
@Service
public class BookingService {
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserServiceClient userServiceClient;
	
	@Autowired
	private PackageServiceClient packageServiceClient;
	
	@Transactional
	public BookingDTO saveBooking(BookingDTO bookingDTO) {
		// Validate user exists
		try {
			UserDTO user = userServiceClient.getUserById(bookingDTO.getUserId());
			if (user == null || user.getUserId() == null) {
				throw new RuntimeException("User not found with ID: " + bookingDTO.getUserId());
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to validate user: " + e.getMessage());
		}
		
		// Validate package exists and is active
		try {
			PackageDTO packageInfo = packageServiceClient.getPackageById(bookingDTO.getPackageId());
			if (packageInfo == null || packageInfo.getPackageId() == null) {
				throw new RuntimeException("Package not found with ID: " + bookingDTO.getPackageId());
			}
			if (!packageInfo.getActive()) {
				throw new RuntimeException("Package is not active: " + bookingDTO.getPackageId());
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to validate package: " + e.getMessage());
		}
		
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
	
	public List<BookingDTO> getBookingsByUserId(Long userId) {
		return bookingRepo.findByUserId(userId).stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .toList();
	}
	
	public BookingDTO getBookingByUserIdAndPackageId(Long userId, Long packageId) {
		return bookingRepo.findByUserIdAndPackageId(userId, packageId)
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .orElse(null);
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
