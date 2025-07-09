package com.example.demo.dto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private Long cartId;
    private String paymentMethod;
    private String shippingMethod;
    private String shippingAddress;
    private String notes;
}
