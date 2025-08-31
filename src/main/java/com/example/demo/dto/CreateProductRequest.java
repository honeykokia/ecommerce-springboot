package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private Integer originalPrice;
    private Long categoryId;
    private Long promotionId;
    private String imageURL;
    private Integer inStock;
    private Double rating;
    private Integer soldCount;
    private String shortDescription;
}
