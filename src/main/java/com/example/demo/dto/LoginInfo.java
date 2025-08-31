package com.example.demo.dto;

import com.example.demo.enums.UserRole;

import lombok.Data;

@Data
public class LoginInfo {

    private String name;
    private String token;
    private UserRole role;
}
