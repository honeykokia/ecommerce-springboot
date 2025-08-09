package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CategoryInfo;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.responses.ApiResponse;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public ApiResponse getCategories() {
        List<CategoryInfo> categoryList = categoryRepository.findAllCategoryInfo();
        return new ApiResponse(Map.of("categories", categoryList));
    }

}
