package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginInfo;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdatePasswordRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.responses.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName("Test");
        loginInfo.setEmail("test@gmail.com");
        loginInfo.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        ApiResponse response = new ApiResponse(loginInfo);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("Test");
        userInfo.setEmail("test@gmail.com");

        ApiResponse response = new ApiResponse(userInfo);
        return ResponseEntity.ok().body(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getProfile() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("Test");
        userInfo.setEmail("test@gmail.com");

        ApiResponse response = new ApiResponse(userInfo);
        return ResponseEntity.ok().body(response);
    }
    
    @PutMapping("/me")
    public ResponseEntity<?> editProfile(@RequestBody UpdateUserRequest updateUserRequest) {

        ApiResponse response = new ApiResponse(updateUserRequest);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {

        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable String token) {

        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }
    

}
