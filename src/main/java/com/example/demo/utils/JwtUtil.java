package com.example.demo.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
    public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public static String generateToken(String email,Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .signWith(SECRET_KEY)
                .setExpiration(new java.util.Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID
     */
    public static Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the email from the JWT token.
     *
     * @param token the JWT token
     * @return the email
     */
    public String getEmailFromToken(String token){
        return extractClaims(token).getSubject();
    }

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID
     */
    public Long getUserIdFromToken(String token) {
        return extractClaims(token).get("userId", Long.class);
    }

    /**
     * Validates the JWT token.
     *
     * @param token the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }

}
