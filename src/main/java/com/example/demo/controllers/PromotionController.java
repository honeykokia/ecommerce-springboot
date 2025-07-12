package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PromotionInfo;
import com.example.demo.responses.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/promotions")
public class PromotionController {

    @GetMapping
    public ResponseEntity<?> getPromotions() {

        PromotionInfo promotionInfo = new PromotionInfo();
        promotionInfo.setId(1L);
        promotionInfo.setName("夏季大促銷");
        promotionInfo.setDiscountType("PERCENTAGE");
        promotionInfo.setDiscountValue(50.0);
        promotionInfo.setDescription("全館商品最高折扣50%");
        promotionInfo.setStartDate(LocalDateTime.of(2025, 6, 1, 0, 0, 0));
        promotionInfo.setEndDate(LocalDateTime.of(2025, 6, 30, 23, 59, 59));
        promotionInfo.setImageUrl("/upload/event.jpg");
        promotionInfo.setIsActive(true);

        ApiResponse response = new ApiResponse(Map.of("promotions", List.of(promotionInfo)));
        return ResponseEntity.ok(response);
    }

}