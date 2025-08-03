package com.example.demo.bean;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.enums.UserRole;

@Entity
@Table(name = "users")
@Data
public class UserBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String image;
    
    private Byte gender;
    
    private LocalDate birthday;
    
    private String phone;
    
    private String city;
    
    private String country;
    
    private String address;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
}