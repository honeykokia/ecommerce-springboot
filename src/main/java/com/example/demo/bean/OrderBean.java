package com.example.demo.bean;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.ShippingMethod;
import com.example.demo.enums.ShippingStatus;

@Entity
@Table(name = "orders")
@Data
public class OrderBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
    
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method", nullable = false)
    private ShippingMethod shippingMethod;
    
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_status", nullable = false)
    private ShippingStatus shippingStatus;
    
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Foreign key relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserBean user;
}