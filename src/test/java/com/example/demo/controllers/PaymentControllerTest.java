package com.example.demo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.responses.ApiResponse;
import com.example.demo.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PaymentController.class)
@ActiveProfiles("test")
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testInitiatePayment() throws Exception {
        // Arrange
        Map<String, Object> requestBody = Map.of(
            "orderId", 1L,
            "paymentMethod", "CREDIT_CARD",
            "amount", 4000
        );

        ApiResponse mockResponse = new ApiResponse(Map.of(
            "message", "Payment initiated successfully",
            "paymentId", 1L,
            "transactionId", "TXN-123456789ABC",
            "status", "PENDING"
        ));

        when(paymentService.initiatePayment(any())).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/payments/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("Payment initiated successfully"))
                .andExpect(jsonPath("$.data.paymentId").value(1))
                .andExpect(jsonPath("$.data.transactionId").value("TXN-123456789ABC"))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    public void testMockPayment() throws Exception {
        // Arrange
        Map<String, Object> requestBody = Map.of(
            "orderId", 1L,
            "status", "SUCCESS"
        );

        ApiResponse mockResponse = new ApiResponse(Map.of(
            "message", "Mock payment processed successfully",
            "paymentId", 1L,
            "orderId", 1L,
            "status", "COMPLETED",
            "transactionId", "TXN-123456789ABC"
        ));

        when(paymentService.processMockPayment(any())).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/payments/mock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("Mock payment processed successfully"))
                .andExpect(jsonPath("$.data.paymentId").value(1))
                .andExpect(jsonPath("$.data.orderId").value(1))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.transactionId").value("TXN-123456789ABC"));
    }
}