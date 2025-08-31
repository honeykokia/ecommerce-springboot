package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.ShippingMethod;
import com.example.demo.enums.ShippingStatus;

import lombok.Data;

@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private String MerchantTradeNo;
    private Integer amountCents;
    private String currency;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime cancelledAt;
    private LocalDateTime paidAt;
    private ShippingMethod shippingMethod;
    private String shippingAddress;
    private ShippingStatus shippingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
