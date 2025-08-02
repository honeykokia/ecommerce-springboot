package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ForgetPasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdatePasswordRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.responses.ApiResponse;
import com.example.demo.services.UserService;
import com.example.demo.utils.VaildationHelper;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
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

    @Autowired
    private VaildationHelper validationHelper;
    @Autowired
    private UserService userService;

    /*
     * 用戶登入
     * @Valid 用於驗證 LoginRequest 中的字段
     * @param loginRequest
     * @param result
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest , BindingResult result) {

        validationHelper.validateOrThrow(result);
    
        ApiResponse response = userService.login(loginRequest);
        return ResponseEntity.ok().body(response);
    }

    /*
     * 註冊新用戶 <Complete>
     * @Valid 用於驗證 RegisterRequest 中的字段
     * BindingResult 用於捕獲驗證錯誤
     */
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result) {
        validationHelper.validateOrThrow(result);
        ApiResponse response = userService.register(registerRequest);
        return ResponseEntity.ok().body(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal Long userId) {

        ApiResponse response = userService.getProfile(userId);
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

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {

        ApiResponse response =new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }
    

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable String token) {

        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }
    

}
