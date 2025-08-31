package com.example.demo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @JsonIgnore
    @Schema(hidden = true)
    private Long userId;

    @NotBlank(message = "Name is required")
    private String name;
    
    private Byte gender;

    @Past(message = "birthday must be a past date")
    private LocalDate birthday;

    private String phone;
    
    private String city;

    private String country;

    private String address;
}
