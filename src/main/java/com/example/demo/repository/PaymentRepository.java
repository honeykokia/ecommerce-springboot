package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.PaymentBean;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentBean, Long> {
    
    // Find payment by order ID
    Optional<PaymentBean> findByOrderId(Long orderId);
    
    // Check if payment exists for an order
    boolean existsByOrderId(Long orderId);
    
    // Find payment by transaction ID
    Optional<PaymentBean> findByTransactionId(String transactionId);
}