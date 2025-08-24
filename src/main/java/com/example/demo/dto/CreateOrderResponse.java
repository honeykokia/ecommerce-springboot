package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateOrderResponse {
    private Long orderId;
    private String merchantTradeNo;
    private int amountCents;
    private String itemName;
    private String tradeDesc;
}

