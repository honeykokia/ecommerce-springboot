package com.example.demo.dto;

import com.example.demo.vaildator.UniqueEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "信箱未輸入")
    @Email(message = "信箱格式不正確")
    @UniqueEmail
    private String email;

    @NotBlank(message = "密碼未輸入")
    private String password;
}
