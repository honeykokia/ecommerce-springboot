package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CartInfo {
    private Long productId;
    private Long userId;
    private Integer quantity;
    private Integer unitPrice;
    private LocalDateTime createAt;  // Note: Keep same name as API spec
}
