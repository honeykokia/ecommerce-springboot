package com.example.demo.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ErrorInfo {
    private final Map<String, String> errors;

    public ErrorInfo() {
        this.errors = new HashMap<>();
    }
    public void addError(String field, String message) {
        this.errors.put(field, message);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Map<String, String> getAll() {
        return Collections.unmodifiableMap(errors);
    }
}
