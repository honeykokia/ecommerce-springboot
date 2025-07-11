package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 業務邏輯異常類
 * Custom business exception for handling business logic errors
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    private final HttpStatus httpStatus;
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    // 常用的業務異常工厂方法 - Common business exception factory methods
    
    public static BusinessException userNotFound(String userId) {
        return new BusinessException("USER_NOT_FOUND", 
            String.format("用戶不存在: %s", userId), HttpStatus.NOT_FOUND);
    }
    
    public static BusinessException productNotFound(String productId) {
        return new BusinessException("PRODUCT_NOT_FOUND", 
            String.format("商品不存在: %s", productId), HttpStatus.NOT_FOUND);
    }
    
    public static BusinessException insufficientStock(String productId) {
        return new BusinessException("INSUFFICIENT_STOCK", 
            String.format("商品庫存不足: %s", productId), HttpStatus.BAD_REQUEST);
    }
    
    public static BusinessException unauthorizedAccess() {
        return new BusinessException("UNAUTHORIZED_ACCESS", 
            "未授權的訪問", HttpStatus.UNAUTHORIZED);
    }
    
    public static BusinessException invalidCredentials() {
        return new BusinessException("INVALID_CREDENTIALS", 
            "用戶名或密碼錯誤", HttpStatus.UNAUTHORIZED);
    }
    
    public static BusinessException orderNotFound(String orderId) {
        return new BusinessException("ORDER_NOT_FOUND", 
            String.format("訂單不存在: %s", orderId), HttpStatus.NOT_FOUND);
    }
    
    public static BusinessException paymentFailed(String reason) {
        return new BusinessException("PAYMENT_FAILED", 
            String.format("付款失敗: %s", reason), HttpStatus.BAD_REQUEST);
    }
}