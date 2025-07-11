package com.example.demo.exception;

import com.example.demo.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 全域錯誤攔截處理器
 * Global Exception Handler for handling all exceptions across the application
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理驗證錯誤 - Handle validation errors from @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        log.warn("Validation error occurred: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "VALIDATION_ERROR");
        errorDetails.put("message", "請求參數驗證失敗");
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        errorDetails.put("fields", fieldErrors);
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 處理非法參數異常 - Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        log.warn("Illegal argument error: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "INVALID_ARGUMENT");
        errorDetails.put("message", ex.getMessage() != null ? ex.getMessage() : "無效的請求參數");
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 處理參數類型錯誤 - Handle method argument type mismatch
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        log.warn("Type mismatch error: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "TYPE_MISMATCH");
        errorDetails.put("message", String.format("參數 '%s' 的類型不正確", ex.getName()));
        errorDetails.put("parameter", ex.getName());
        errorDetails.put("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 處理缺少必要參數 - Handle missing required parameters
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        log.warn("Missing parameter error: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "MISSING_PARAMETER");
        errorDetails.put("message", String.format("缺少必要參數: %s", ex.getParameterName()));
        errorDetails.put("parameter", ex.getParameterName());
        errorDetails.put("parameterType", ex.getParameterType());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 處理HTTP請求體解析錯誤 - Handle malformed JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        log.warn("HTTP message not readable: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "MALFORMED_JSON");
        errorDetails.put("message", "請求體格式錯誤，請檢查JSON格式");
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 處理不支援的HTTP方法 - Handle unsupported HTTP methods
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
        log.warn("Method not supported: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "METHOD_NOT_SUPPORTED");
        errorDetails.put("message", String.format("不支援的HTTP方法: %s", ex.getMethod()));
        errorDetails.put("method", ex.getMethod());
        errorDetails.put("supportedMethods", ex.getSupportedMethods());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 處理404錯誤 - Handle 404 Not Found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        log.warn("No handler found: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "NOT_FOUND");
        errorDetails.put("message", "請求的資源不存在");
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("method", ex.getHttpMethod());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 處理業務邏輯異常 - Handle custom business exceptions
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        log.warn("Business exception: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getErrorCode());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", ex.getHttpStatus().value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    /**
     * 處理運行時異常 - Handle runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        
        log.error("Runtime exception occurred: ", ex);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "RUNTIME_ERROR");
        errorDetails.put("message", "系統發生錯誤，請稍後再試");
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 處理所有其他未捕獲的異常 - Handle all other uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(
            Exception ex, HttpServletRequest request) {
        
        log.error("Unexpected exception occurred: ", ex);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "INTERNAL_SERVER_ERROR");
        errorDetails.put("message", "系統發生未預期的錯誤");
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        ApiResponse response = new ApiResponse(errorDetails);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}