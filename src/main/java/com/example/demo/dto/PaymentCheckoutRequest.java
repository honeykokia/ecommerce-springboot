package com.example.demo.dto;

import lombok.Data;

@Data
public class PaymentCheckoutRequest {
    private String merchantTradeNo;
    private Integer amountCents;
    private String itemName;
    private String tradeDesc;
}
