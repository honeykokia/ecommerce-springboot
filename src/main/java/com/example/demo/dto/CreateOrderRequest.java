package com.example.demo.dto;

import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.ShippingMethod;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private PaymentMethod paymentMethod;
    private ShippingMethod shippingMethod;
    private String shippingAddress;
    private String tradeDesc;
}
