package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.TokenBean;
import com.example.demo.enums.TokenType;

@Repository
public interface TokenRepository extends JpaRepository<TokenBean, Long> {
    // Additional query methods can be defined here if needed
    Optional<TokenBean> findByToken(String token);

    TokenBean findByUserIdAndTokenType(Long userId, TokenType tokenType);

}
