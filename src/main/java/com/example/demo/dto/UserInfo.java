package com.example.demo.dto;

import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;

import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String name;
    private UserStatus status;
    private UserRole role;
}
