package com.example.demo.dto;

import lombok.Data;

@Data
public class UpdateOrderRequest {
    private String status; // PENDING, PAID, SHIPPED, COMPLETED, CANCELED
    private String shippingMethod;
    private String shippingAddress;
}