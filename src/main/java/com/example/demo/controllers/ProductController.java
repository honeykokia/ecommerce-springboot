package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductInfo;
import com.example.demo.dto.TagInfo;
import com.example.demo.responses.ApiResponse;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam(required = false) String name , @RequestParam(required = false) Integer categoryId) {

        TagInfo tag1 = new TagInfo();
        tag1.setId(1L);
        tag1.setName("熱賣");

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(1L);
        productInfo.setName("藍芽耳機");
        productInfo.setPrice(2000);
        productInfo.setCategoryId(1L);
        productInfo.setPromotionId(1L);
        productInfo.setImageURL("/upload/defaultProduct.jpg");
        productInfo.setInStock(20);
        productInfo.setRating(4.5);
        productInfo.setSoldCount(36);
        productInfo.setShortDescription("這是一款高品質的藍芽耳機，提供卓越的音質和舒適的佩戴體驗。");
        productInfo.setTags(List.of(tag1));

        ApiResponse response = new ApiResponse(Map.of("products",List.of(productInfo)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {


        TagInfo tag1 = new TagInfo();
        tag1.setId(1L);
        tag1.setName("熱賣");

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(1L);
        productInfo.setName("藍芽耳機");
        productInfo.setPrice(2000);
        productInfo.setCategoryId(1L);
        productInfo.setPromotionId(1L);
        productInfo.setImageURL("/upload/defaultProduct.jpg");
        productInfo.setInStock(20);
        productInfo.setRating(4.5);
        productInfo.setSoldCount(36);
        productInfo.setShortDescription("這是一款高品質的藍芽耳機，提供卓越的音質和舒適的佩戴體驗。");
        productInfo.setTags(List.of(tag1));

        ApiResponse response = new ApiResponse(productInfo);
        return ResponseEntity.ok(response);
    }

}
