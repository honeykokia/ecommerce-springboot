package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgetPasswordRequest {
    @NotBlank(message = "Email is required")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

}
