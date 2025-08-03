package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryInfo;
import com.example.demo.responses.ApiResponse;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<?> getCategories() {
        
        CategoryInfo category1 = new CategoryInfo();
        category1.setId(1L);
        category1.setName("耳機");
        category1.setDescription("各式藍芽耳機");

        ApiResponse response = new ApiResponse(Map.of("categories", List.of(category1)));
        return ResponseEntity.ok(response);
    }

}