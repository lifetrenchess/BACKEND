package cts.travelpackagebookingsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import cts.travelpackagebookingsystem.enums.BookingStatus;
import cts.travelpackagebookingsystem.model.BookingDTO;
import cts.travelpackagebookingsystem.service.BookingService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDTO bookingDTO;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // ✅ Ensures proper serialization for LocalDate

        // ✅ Ensure BookingDTO is properly initialized with all required fields
        bookingDTO = new BookingDTO(1L, 101L, 201L, 
                LocalDate.of(2025, 6, 11), 
                LocalDate.of(2025, 6, 25), 
                BookingStatus.CONFIRMED, 
                null);
    }

    @Test
    void testCreateBooking() throws Exception {
        when(bookingService.saveBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andDo(print()) // ✅ Logs full response for debugging
                .andExpect(status().isCreated()) // ✅ Ensure correct HTTP status is returned
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void testGetAllBookings() throws Exception {
        List<BookingDTO> list = Arrays.asList(bookingDTO);
        when(bookingService.getAllBookings()).thenReturn(list);

        mockMvc.perform(get("/api/bookings"))
                .andDo(print()) // ✅ Logs response for debugging
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(list.size()));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(bookingDTO);

        mockMvc.perform(get("/api/bookings/1"))
                .andDo(print()) // ✅ Debugging log
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(101L));
    }

    @Test
    void testUpdateBooking() throws Exception {
        BookingDTO updatedDTO = new BookingDTO(1L, 101L, 201L, 
                LocalDate.of(2025, 6, 15), 
                LocalDate.of(2025, 6, 30), 
                BookingStatus.PENDING, 
                null);

        when(bookingService.updateBooking(eq(1L), any(BookingDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andDo(print()) // ✅ Debugging log
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testDeleteBooking() throws Exception {
        doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andDo(print()) // ✅ Debugging log
                .andExpect(status().isOk());
    }
}
