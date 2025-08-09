package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.CartBean;

@Repository
public interface CartRepository extends JpaRepository<CartBean, Long> {
    // Find all cart items for a specific user
    List<CartBean> findByUserId(Long userId);
    
    // Find specific cart item by user and product
    Optional<CartBean> findByUserIdAndProductId(Long userId, Long productId);
    
    // Delete all cart items for a specific user
    void deleteByUserId(Long userId);
    
    // Delete specific cart item by user and product
    void deleteByUserIdAndProductId(Long userId, Long productId);
    
    // Check if product exists in user's cart
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}