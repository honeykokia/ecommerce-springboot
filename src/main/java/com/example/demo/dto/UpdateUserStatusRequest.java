package com.example.demo.dto;

import com.example.demo.enums.UserStatus;

import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    private UserStatus status;
}
