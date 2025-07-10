package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UpdateUserStatusRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.responses.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@gmail.com");
        userInfo.setName("Test");

        ApiResponse response = new ApiResponse(Map.of("users", List.of(userInfo)));

        return ResponseEntity.ok().body(response);
    }
    
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId, @RequestBody UpdateUserStatusRequest updateUserStatusRequest) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }
}