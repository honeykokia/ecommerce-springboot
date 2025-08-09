package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PaymentInitiateRequest;
import com.example.demo.dto.PaymentMockRequest;
import com.example.demo.responses.ApiResponse;
import com.example.demo.services.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse> initiatePayment(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PaymentInitiateRequest request) {
        ApiResponse response = paymentService.initiatePayment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mock")
    public ResponseEntity<ApiResponse> mockPayment(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PaymentMockRequest request) {
        ApiResponse response = paymentService.processMockPayment(request);
        return ResponseEntity.ok(response);
    }
}