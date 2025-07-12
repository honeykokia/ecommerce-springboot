package com.example.demo.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBean {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserBean user;
    
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
}