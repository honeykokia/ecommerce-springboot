package com.example.demo.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBean {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private PromotionBean promotion;
    
    @Column(name = "category_id")
    private Long categoryId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer price;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "in_stock", nullable = false)
    private Integer inStock;
    
    private Double rating;
    
    @Column(name = "sold_count")
    private Integer soldCount;
    
    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;
}