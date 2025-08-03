package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CartInfo {
    private Long id;
    private Long userId;
    private Integer quantity;
    private Integer unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
