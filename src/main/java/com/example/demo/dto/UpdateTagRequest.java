package com.example.demo.dto;

import lombok.Data;

@Data
public class UpdateTagRequest {
    private String name;
    private String description;
    private String color;
}
