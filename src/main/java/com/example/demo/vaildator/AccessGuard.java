package com.example.demo.vaildator;

import org.springframework.stereotype.Component;

import com.example.demo.enums.UserRole;
import com.example.demo.repository.UserRepository;

@Component
public class AccessGuard {
    private final UserRepository userRepository;

    public AccessGuard(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean hasRole(Long userId, UserRole requiredRole) {
        return userRepository.findById(userId)
                .map(user -> user.getRole() == requiredRole)
                .orElse(false);
    }
}
