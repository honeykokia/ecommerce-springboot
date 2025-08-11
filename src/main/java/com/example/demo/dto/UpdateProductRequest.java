package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateProductRequest {
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
    private List<Integer> tags;
}
