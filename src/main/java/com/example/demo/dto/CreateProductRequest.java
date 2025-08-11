package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private Integer price;
    private Long categoryId;
    private Long promotionId;
    private String imageUrl;
    private Integer inStock;
    private Double rating;
    private Integer soldCount;
    private String shortDescription;
}
