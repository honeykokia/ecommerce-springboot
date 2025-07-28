package com.example.demo.vaildator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailVaildator implements ConstraintValidator<UniqueEmail, String>{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) return true; // 讓 @NotBlank 去處理 null
        return !userRepository.existsByEmail(email);
    }

}
