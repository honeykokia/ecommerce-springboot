package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CartInfo {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Integer unitPrice;
    private LocalDateTime createAt;  // Note: Keep same name as API spec
    private LocalDateTime updateAt;  // Note: Keep same name as API spec
}
