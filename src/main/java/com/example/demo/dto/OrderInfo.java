package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private String orderNumber;
    private OrderStatus status;
    private String paymentMethod;
    private Boolean isPaid;
    private LocalDateTime cancelledAt;
    private LocalDateTime paidAt;
    private String shippingMethod;
    private String shippingAddress;
    private String shippingStatus;
    private Integer totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
