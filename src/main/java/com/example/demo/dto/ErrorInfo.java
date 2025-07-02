package com.example.demo.dto;

import java.util.Map;

public class ErrorInfo {
    private Map<String, String> errors;

    public ErrorInfo(Map<String, String> errors) {
        this.errors = errors;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
