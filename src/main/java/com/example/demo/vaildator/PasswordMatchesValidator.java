package com.example.demo.vaildator;

import org.springframework.stereotype.Component;

import com.example.demo.dto.RegisterRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object>{

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof RegisterRequest request) {
            boolean matched = request.getPassword().equals(request.getConfirmPassword());
            if (!matched) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("密碼與確認密碼不一致")
                        .addPropertyNode("confirmPassword")
                        .addConstraintViolation();
            }
            return matched;
        }
        return false;
    }
}
