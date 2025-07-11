package com.example.demo.controllers;

import com.example.demo.exception.BusinessException;
import com.example.demo.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * 測試控制器 - 用於演示全域錯誤處理器的功能
 * Test controller for demonstrating global exception handler functionality
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 測試驗證錯誤
     */
    @PostMapping("/validation")
    public ResponseEntity<ApiResponse> testValidation(@Valid @RequestBody TestRequest request) {
        ApiResponse response = new ApiResponse(Map.of("message", "驗證成功", "data", request));
        return ResponseEntity.ok(response);
    }

    /**
     * 測試IllegalArgumentException
     */
    @GetMapping("/illegal-argument")
    public ResponseEntity<ApiResponse> testIllegalArgument(@RequestParam String value) {
        if ("error".equals(value)) {
            throw new IllegalArgumentException("這是一個無效的參數值");
        }
        ApiResponse response = new ApiResponse(Map.of("message", "參數正確", "value", value));
        return ResponseEntity.ok(response);
    }

    /**
     * 測試參數類型錯誤
     */
    @GetMapping("/type-mismatch")
    public ResponseEntity<ApiResponse> testTypeMismatch(@RequestParam Integer number) {
        ApiResponse response = new ApiResponse(Map.of("message", "數字正確", "number", number));
        return ResponseEntity.ok(response);
    }

    /**
     * 測試缺少必要參數
     */
    @GetMapping("/missing-param")
    public ResponseEntity<ApiResponse> testMissingParam(@RequestParam String requiredParam) {
        ApiResponse response = new ApiResponse(Map.of("message", "參數完整", "param", requiredParam));
        return ResponseEntity.ok(response);
    }

    /**
     * 測試業務邏輯異常
     */
    @GetMapping("/business-exception")
    public ResponseEntity<ApiResponse> testBusinessException(@RequestParam String type) {
        switch (type) {
            case "user-not-found":
                throw BusinessException.userNotFound("12345");
            case "product-not-found":
                throw BusinessException.productNotFound("PROD001");
            case "insufficient-stock":
                throw BusinessException.insufficientStock("PROD002");
            case "unauthorized":
                throw BusinessException.unauthorizedAccess();
            default:
                ApiResponse response = new ApiResponse(Map.of("message", "業務邏輯正常"));
                return ResponseEntity.ok(response);
        }
    }

    /**
     * 測試運行時異常
     */
    @GetMapping("/runtime-exception")
    public ResponseEntity<ApiResponse> testRuntimeException() {
        throw new RuntimeException("這是一個模擬的運行時異常");
    }

    /**
     * 測試一般異常
     */
    @GetMapping("/general-exception")
    public ResponseEntity<ApiResponse> testGeneralException() throws Exception {
        throw new Exception("這是一個模擬的一般異常");
    }

    /**
     * 測試請求對象
     */
    public static class TestRequest {
        @NotBlank(message = "姓名不能為空")
        private String name;

        @NotBlank(message = "電子郵件不能為空")
        @Email(message = "電子郵件格式不正確")
        private String email;

        @NotNull(message = "年齡不能為空")
        private Integer age;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }
}