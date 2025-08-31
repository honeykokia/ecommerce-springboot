package com.example.demo.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.bean.TokenBean;
import com.example.demo.bean.UserBean;
import com.example.demo.enums.TokenType;
import com.example.demo.repository.TokenRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class TokenService {

    @Value("${app.reset-token.duration-minutes:30}")
    private int defaultDuration;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EntityManager em;

    @Transactional
    public String generateToken(Long userId, TokenType tokenType){

        TokenBean existingToken = tokenRepository.findByUserIdAndTokenType(userId, tokenType);
        if (existingToken != null && !existingToken.isUsed()) {
            // 如果已存在且未使用，則返回已存在的 token
            return existingToken.getToken();
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plus(defaultDuration, ChronoUnit.MINUTES);

        TokenBean tokenBean = new TokenBean();
        tokenBean.setUser(em.getReference(UserBean.class, userId));
        tokenBean.setToken(token);
        tokenBean.setTokenType(tokenType);
        tokenBean.setUsed(false);
        tokenBean.setCreatedAt(LocalDateTime.now());
        tokenBean.setExpiresAt(expiresAt);
        tokenRepository.save(tokenBean);

        return token;
    }   

}
