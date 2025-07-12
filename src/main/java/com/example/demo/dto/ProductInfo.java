package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductInfo {
    private Long id;
    private Long promotionId;
    private Long categoryId;
    private String name;
    private Integer price;
    private String imageUrl;
    private Integer inStock;
    private Double rating;
    private Integer soldCount;
    private String shortDescription;
    private String[] tags;
}
