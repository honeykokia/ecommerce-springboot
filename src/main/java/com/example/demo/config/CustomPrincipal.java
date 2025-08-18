package com.example.demo.config;

import java.util.Set;

import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;

public record CustomPrincipal(
    Long id,
    String email,
    UserStatus status,
    Set<UserRole> roles,
    int tokenVersion
) implements java.security.Principal {
    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
