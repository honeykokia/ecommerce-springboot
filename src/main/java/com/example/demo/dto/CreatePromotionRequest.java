package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.enums.DiscountType;

import lombok.Data;

@Data
public class CreatePromotionRequest {
    private String name;
    private DiscountType discountType;
    private Integer discountValue;
    private String description;
    private String imageUrl;
    private Boolean isActive;
    private LocalDate startDate;
    private LocalDate endDate;

}
