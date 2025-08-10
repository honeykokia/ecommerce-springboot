package com.example.demo.dto;

import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.ShippingMethod;

import lombok.Data;

@Data
public class CheckoutRequest {
    private Long cartId;
    private PaymentMethod paymentMethod;
    private ShippingMethod shippingMethod;
    private String shippingAddress;
    private String notes;
}
