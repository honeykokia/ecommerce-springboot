package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateTagRequest {
    private String name;
    private String description;
    private String color;
}
