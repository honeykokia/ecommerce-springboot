package com.example.demo.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String description;
    private Integer originalPrice;
    private Long categoryId;
    private Long promotionId;
    private Integer inStock;
    private Double rating;
    private Integer soldCount;
    private String shortDescription;
    private List<Integer> tags;
    private MultipartFile image;
}
