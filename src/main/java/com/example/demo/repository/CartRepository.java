package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.CartBean;
import com.example.demo.enums.CartStatus;

@Repository
public interface CartRepository extends JpaRepository<CartBean, Long> {
    // Find all cart items for a specific user
    Optional<CartBean> findByUserIdAndStatus(Long userId, CartStatus status);
    void deleteByUserId(Long userId);
    
}