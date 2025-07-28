package com.example.demo.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.ErrorInfo;
import com.example.demo.exception.ApiException;

@Component
public class VaildationHelper {
    public void validateOrThrow(BindingResult result) {
        if (result.hasErrors()) {
            ErrorInfo errorInfo = new ErrorInfo();
            result.getFieldErrors().forEach(err ->
                errorInfo.addError(err.getField(), err.getDefaultMessage())
            );
            throw new ApiException("Validation failed", 400, errorInfo);
        }
    }
}
