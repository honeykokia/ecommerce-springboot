package com.example.demo.dto;

import lombok.Data;

@Data
public class CartAddItemRequest {
    private Long productId;
    private Integer quantity;
    private Integer unitPrice;
}
