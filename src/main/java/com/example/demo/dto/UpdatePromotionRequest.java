package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.enums.DiscountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePromotionRequest {
    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Discount Type is required")
    private DiscountType discountType;

    @NotNull(message = "Discount Value is required")
    private Integer discountValue;

    private String description;

    @NotNull(message = "Is Active is required")
    
    private Boolean isActive;
    @NotNull(message = "Start Date is required")
    private LocalDate startDate;

    @NotNull(message = "End Date is required")
    private LocalDate endDate;
}
