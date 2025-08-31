package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UpdatePromotionImageRequest {

    @JsonIgnore
    private MultipartFile file;

    @JsonIgnore
    private Long promotionId;

}