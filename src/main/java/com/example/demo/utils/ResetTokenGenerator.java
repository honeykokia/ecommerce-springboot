package com.example.demo.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResetTokenGenerator {

    @Value("${app.reset-token.duration-minutes:30}")
    private int defaultDuration;

    /**
     * 產生忘記密碼 token（預設 30 分鐘有效）
     */
    public ResetTokenData generate() {
        return generate(defaultDuration);
    }

    /**
     * 產生忘記密碼 token（可自訂有效時間）
     *
     * @param durationInMinutes 有效時間（分鐘）
     */
    public ResetTokenData generate(int durationInMinutes) {
        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(durationInMinutes, ChronoUnit.MINUTES);
        return new ResetTokenData(token, expiresAt);
    }

    // 資料封裝類
    public static class ResetTokenData {
        private final String token;
        private final Instant expiresAt;

        public ResetTokenData(String token, Instant expiresAt) {
            this.token = token;
            this.expiresAt = expiresAt;
        }

        public String getToken() {
            return token;
        }

        public Instant getExpiresAt() {
            return expiresAt;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}
