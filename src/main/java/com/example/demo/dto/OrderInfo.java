package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private String orderNumber;
    private String status;
    private String paymentMethod;
    private Boolean isPaid;
    private LocalDateTime paidAt;
    private String shippingAddress;
    private String shippingStatus;
    private Long totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
