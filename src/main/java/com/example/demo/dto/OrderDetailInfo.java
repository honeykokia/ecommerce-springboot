package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderDetailInfo {
    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Long price;
    private Long totalPrice;
}
