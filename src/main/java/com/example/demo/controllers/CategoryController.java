package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryInfo;
import com.example.demo.responses.ApiResponse;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<?> getCategories() {
        CategoryInfo category1 = new CategoryInfo();
        category1.setId(1L);
        category1.setName("耳機");

        CategoryInfo category2 = new CategoryInfo();
        category2.setId(2L);
        category2.setName("手機");

        CategoryInfo category3 = new CategoryInfo();
        category3.setId(3L);
        category3.setName("電腦");

        ApiResponse response = new ApiResponse(Map.of("categories", List.of(category1, category2, category3)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setId(categoryId);
        categoryInfo.setName("耳機");

        ApiResponse response = new ApiResponse(Map.of("category", categoryInfo));
        return ResponseEntity.ok(response);
    }
}