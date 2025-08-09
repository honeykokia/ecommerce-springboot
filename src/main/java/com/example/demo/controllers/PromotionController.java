package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PromotionInfo;
import com.example.demo.responses.ApiResponse;
import com.example.demo.services.PromotionService;


@RestController
@RequestMapping("/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping
    public ResponseEntity<?> getPromotions() {

        ApiResponse response = promotionService.getPromotions();
        return ResponseEntity.ok(response);
    }

}