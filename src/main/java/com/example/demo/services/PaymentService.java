package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.PaymentBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.PaymentInitiateRequest;
import com.example.demo.dto.PaymentMockRequest;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.responses.ApiResponse;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Initiate payment for an order
     */
    @Transactional
    public ApiResponse initiatePayment(PaymentInitiateRequest request) {
        try {
            // Check if payment already exists for this order
            if (paymentRepository.existsByOrderId(request.getOrderId())) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.addError("orderId", "Payment already initiated for this order");
                throw new ApiException("Payment initiation failed", 400, errorInfo);
            }

            // Create new payment record
            PaymentBean payment = new PaymentBean();
            payment.setOrderId(request.getOrderId());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setAmount(request.getAmount());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setTransactionId(generateTransactionId());
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());

            PaymentBean savedPayment = paymentRepository.save(payment);

            return new ApiResponse(Map.of(
                "message", "Payment initiated successfully",
                "paymentId", savedPayment.getId(),
                "transactionId", savedPayment.getTransactionId(),
                "status", savedPayment.getStatus()
            ));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("general", "Failed to initiate payment");
            throw new ApiException("Payment initiation failed", 400, errorInfo);
        }
    }

    /**
     * Process mock payment for testing
     */
    @Transactional
    public ApiResponse processMockPayment(PaymentMockRequest request) {
        try {
            // Find existing payment for the order
            PaymentBean payment = paymentRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.addError("orderId", "No payment found for this order");
                    return new ApiException("Mock payment failed", 400, errorInfo);
                });

            // Update payment status based on mock request
            if ("SUCCESS".equals(request.getStatus())) {
                payment.setStatus(PaymentStatus.COMPLETED);
            } else if ("FAILED".equals(request.getStatus())) {
                payment.setStatus(PaymentStatus.FAILED);
            } else {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.addError("status", "Invalid status. Must be SUCCESS or FAILED");
                throw new ApiException("Mock payment failed", 400, errorInfo);
            }

            payment.setUpdatedAt(LocalDateTime.now());
            PaymentBean updatedPayment = paymentRepository.save(payment);

            return new ApiResponse(Map.of(
                "message", "Mock payment processed successfully",
                "paymentId", updatedPayment.getId(),
                "orderId", updatedPayment.getOrderId(),
                "status", updatedPayment.getStatus(),
                "transactionId", updatedPayment.getTransactionId()
            ));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("general", "Failed to process mock payment");
            throw new ApiException("Mock payment failed", 400, errorInfo);
        }
    }

    /**
     * Generate a unique transaction ID
     */
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}