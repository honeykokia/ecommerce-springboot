package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserAvatarRequqst {

    @JsonIgnore
    private MultipartFile file;

    @JsonIgnore
    private Long userId;

    @NotBlank(message = "Image is required")
    private String image;
}
