package cts.travelpackagebookingsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import cts.travelpackagebookingsystem.entity.Booking;
import cts.travelpackagebookingsystem.entity.Payment;
import cts.travelpackagebookingsystem.enums.BookingStatus;
import cts.travelpackagebookingsystem.enums.PaymentStatus;
import cts.travelpackagebookingsystem.exception.ResourceNotFoundException;
import cts.travelpackagebookingsystem.model.BookingDTO;
import cts.travelpackagebookingsystem.model.PaymentDTO;
import cts.travelpackagebookingsystem.repository.BookingRepository;
import cts.travelpackagebookingsystem.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentDTO paymentDto;
    private Payment payment;
    private BookingDTO bookingDto;
    private Booking booking;

    @BeforeEach
    void setUp() {
       
        bookingDto = new BookingDTO(1L, 101L, 201L, 
                LocalDate.of(2025, 6, 11), 
                LocalDate.of(2025, 6, 25), 
                BookingStatus.CONFIRMED, 
                null);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setUserId(101L);
        booking.setPackageId(201L);
        booking.setStartDate(LocalDate.of(2025, 6, 11));
        booking.setEndDate(LocalDate.of(2025, 6, 25));
        booking.setStatus(BookingStatus.CONFIRMED);

       
        paymentDto = new PaymentDTO(1001L, 2001L, 575770.00, "PAID", "Credit Card", booking.getBookingId());

        payment = new Payment();
        payment.setPaymentId(1001L);
        payment.setUserId(2001L);
        payment.setAmount(575770.00);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentMethod("Credit Card");
        payment.setBooking(booking);
    }

    @Test
    void testCreatePayment() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(mapper.map(paymentDto, Payment.class)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(mapper.map(payment, PaymentDTO.class)).thenReturn(paymentDto);

        PaymentDTO result = paymentService.createPayment(paymentDto);

        assertNotNull(result);
        assertEquals(booking.getBookingId(), result.getBookingId()); // Assert bookingId directly
        assertEquals("PAID", result.getStatus());
        assertEquals("Credit Card", result.getPaymentMethod());
    }

    @Test
    void testGetPaymentById() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(mapper.map(payment, PaymentDTO.class)).thenReturn(paymentDto);

        PaymentDTO result = paymentService.getPaymentById(1001L);

        assertNotNull(result);
        assertEquals(575770.00, result.getAmount());
        assertEquals("PAID", result.getStatus());
    }

    @Test
    void testGetPaymentByIdThrowsException() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            paymentService.getPaymentById(9999L);
        });
    }

    @Test
    void testUpdatePayment() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any())).thenReturn(payment);
        when(mapper.map(payment, PaymentDTO.class)).thenReturn(paymentDto);

        PaymentDTO result = paymentService.updatePayment(1001L, paymentDto);

        assertNotNull(result);
        assertEquals("PAID", result.getStatus());
        assertEquals("Credit Card", result.getPaymentMethod());
    }
}
