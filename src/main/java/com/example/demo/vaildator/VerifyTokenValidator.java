package com.example.demo.vaildator;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.demo.bean.TokenBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.TokenRepository;

@Component
public class VerifyTokenValidator implements CustValidator<String, TokenBean> {

    private final TokenRepository tokenRepository;
    
    public VerifyTokenValidator(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public boolean support(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public TokenBean validate(String token) {

        TokenBean tokenBean = tokenRepository.findByToken(token)
                .orElseThrow(() -> { 
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.addError(token, "The token is invalid or expired.");
                    throw new ApiException("Token not found", 404, errorInfo);
                });

        if (tokenBean.isUsed()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError(token, "The token has already been used.");
            throw new ApiException("Validation failed", 400, errorInfo);
        }

        if (tokenBean.getExpiresAt().isBefore(LocalDateTime.now())) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError(token, "The token has expired.");
            throw new ApiException("Validation failed", 400, errorInfo);
        }


        return tokenBean;
    }

}
