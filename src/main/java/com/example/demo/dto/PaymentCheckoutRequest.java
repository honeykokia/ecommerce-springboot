package com.example.demo.dto;

import lombok.Data;

@Data
public class PaymentCheckoutRequest {
    private String orderId;
    private int amount;
    private String itemName;
    private String tradeDesc;
}
