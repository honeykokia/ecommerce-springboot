package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductInfo {
    private Long id;
    private Long promotionId;
    private Long categoryId;
    private String name;
    private Integer originalPrice;
    private Integer finalPrice;
    private String imageURL;
    private Integer inStock;
    private Double rating;
    private Integer soldCount;
    private String shortDescription;
    private List<TagInfo> tags;
}
