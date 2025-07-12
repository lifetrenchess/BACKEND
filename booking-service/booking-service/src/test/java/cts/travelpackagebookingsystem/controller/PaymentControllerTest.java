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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Arrays;
import java.util.List;

import cts.travelpackagebookingsystem.model.PaymentDTO;
import cts.travelpackagebookingsystem.service.PaymentService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentDTO paymentDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 

        paymentDTO = new PaymentDTO(1L, 101L, 575770.00, "PAID", "Credit Card", null);
    }

    @Test
    void testCreatePayment() throws Exception {
        when(paymentService.createPayment(any(PaymentDTO.class))).thenReturn(paymentDTO);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void testGetAllPayments() throws Exception {
        List<PaymentDTO> list = Arrays.asList(paymentDTO);
        when(paymentService.getAllPayment()).thenReturn(list);

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(list.size()));
    }

    @Test
    void testGetPaymentById() throws Exception {
        when(paymentService.getPaymentById(1L)).thenReturn(paymentDTO);

        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void testUpdatePayment() throws Exception {
        PaymentDTO updatedDTO = new PaymentDTO(1L, 101L, 575770.00, "UPDATED", "UPI", null);

        when(paymentService.updatePayment(eq(1L), any(PaymentDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/payments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethod").value("UPI"));
    }

    @Test
    void testDeletePayment() throws Exception {
        doNothing().when(paymentService).deletePayment(1L);

        mockMvc.perform(delete("/api/payments/1"))
                .andExpect(status().isOk());
    }
}
