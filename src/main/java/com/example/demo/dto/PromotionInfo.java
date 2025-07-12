package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PromotionInfo {
    private Long id;
    private String name;
    private String discountType;
    private Double discountValue;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String imageUrl;
    private Boolean isActive;
}