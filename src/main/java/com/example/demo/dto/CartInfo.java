package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CartInfo {
    private Long cartItemId;
    private Long userId;
    private LocalDateTime createAt;  // Note: Keep same name as API spec
}
