package com.example.demo.bean;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.PaymentStatus;

@Entity
@Table(name = "payments")
@Data
public class PaymentBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    
    @Column(nullable = false)
    private Integer amount;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Foreign key relationship
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, insertable = false, updatable = false)
    private OrderBean order;
}