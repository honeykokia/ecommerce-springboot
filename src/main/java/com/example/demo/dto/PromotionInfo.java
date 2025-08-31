package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.enums.DiscountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionInfo {
    private Long id;
    private String name;
    private DiscountType discountType;
    private Integer discountValue;
    private String description;
    private String imageURL;
    private Boolean isActive;
    private LocalDate startDate;
    private LocalDate endDate;
}