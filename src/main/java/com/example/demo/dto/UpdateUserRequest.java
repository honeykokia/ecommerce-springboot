package com.example.demo.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @JsonIgnore
    @Schema(hidden = true)
    private Long userId;

    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Gender is required")
    private Byte gender;

    @NotNull(message = "birthday is required")
    private LocalDate birthday;

    @NotBlank(message = "phone is required")
    private String phone;
    
    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "country is required")
    private String country;

    @NotBlank(message = "address is required")
    private String address;
}
