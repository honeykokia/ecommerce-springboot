package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.responses.ApiResponse;
import com.example.demo.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategories() {
        ApiResponse response = categoryService.getCategories();

        // category1.setName("耳機");
        // category1.setDescription("各式藍芽耳機");
        // ApiResponse response = new ApiResponse(Map.of("categories", List.of(category1)));
        return ResponseEntity.ok(response);
    }

}