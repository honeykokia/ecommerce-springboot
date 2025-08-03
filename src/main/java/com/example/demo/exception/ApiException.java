package com.example.demo.exception;

import java.util.Map;

import com.example.demo.dto.ErrorInfo;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final Map<String,Object> data;

    public ApiException(String message, int statusCode, ErrorInfo errorInfo) {
        super(message);
        this.statusCode = statusCode;
        this.data = errorInfo != null ? Map.of("errors",errorInfo.getAll()) : null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, Object> getData() {
        return data;
    }   

}
