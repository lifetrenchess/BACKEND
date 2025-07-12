package cts.travelpackagebookingsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cts.travelpackagebookingsystem.entity.Booking;
import cts.travelpackagebookingsystem.enums.BookingStatus;
import cts.travelpackagebookingsystem.exception.ResourceNotFoundException;
import cts.travelpackagebookingsystem.model.BookingDTO;
import cts.travelpackagebookingsystem.repository.BookingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private BookingDTO bookingDto;
    private Booking booking;

    @BeforeEach
    void setUp() {
        
        bookingDto = new BookingDTO();
        bookingDto.setBookingId(1L);
        bookingDto.setUserId(101L);
        bookingDto.setPackageId(201L);
        bookingDto.setStartDate(LocalDate.of(2025, 6, 11));
        bookingDto.setEndDate(LocalDate.of(2025, 6, 25));
        bookingDto.setStatus(BookingStatus.CONFIRMED); 


        
        booking = new Booking();
        booking.setBookingId(1L);
        booking.setUserId(101L);
        booking.setPackageId(201L);
        booking.setStartDate(LocalDate.of(2025, 6, 11));
        booking.setEndDate(LocalDate.of(2025, 6, 25));
        bookingDto.setStatus(BookingStatus.CONFIRMED); 

    }

    @Test
    void testCreateBooking() {
        when(mapper.map(bookingDto, Booking.class)).thenReturn(booking);
        when(bookingRepository.save(any())).thenReturn(booking);
        when(mapper.map(booking, BookingDTO.class)).thenReturn(bookingDto);

        BookingDTO result = bookingService.saveBooking(bookingDto);

        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(101L, result.getUserId());
    }

    @Test
    void testGetBookingById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(mapper.map(booking, BookingDTO.class)).thenReturn(bookingDto);

        BookingDTO result = bookingService.getBookingById(1L);

        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(201L, result.getPackageId());
    }

    @Test
    void testGetBookingByIdThrowsException() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.getBookingById(9999L);
        });
    }

    @Test
    void testUpdateBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(mapper.map(booking, BookingDTO.class)).thenReturn(bookingDto);

        BookingDTO result = bookingService.updateBooking(1L, bookingDto);

        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void testGetAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);

        when(bookingRepository.findAll()).thenReturn(bookingList);
        when(mapper.map(booking, BookingDTO.class)).thenReturn(bookingDto);

        List<BookingDTO> result = bookingService.getAllBookings();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(BookingStatus.CONFIRMED, result.get(0).getStatus());
    }
}
