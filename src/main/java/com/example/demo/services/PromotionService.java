package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PromotionInfo;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.responses.ApiResponse;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public ApiResponse getPromotions() {

        List<PromotionInfo> promotionList = promotionRepository.findAllPromotionInfo();

        return new ApiResponse(Map.of("promotions", promotionList));
    }
}