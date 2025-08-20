package com.example.demo.dto;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String name;
    private String description;
}