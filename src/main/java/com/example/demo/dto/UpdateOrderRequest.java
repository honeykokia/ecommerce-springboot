package com.example.demo.dto;

import lombok.Data;

@Data
public class UpdateOrderRequest {
    private String status;
    private String shippingMethod;
    private String shippingAddress;
}