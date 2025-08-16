package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateOrderResponse {
    private String merchant_trade_no;
    private int amountCents;
    private String itemName;
    private String tradeDesc;
}

