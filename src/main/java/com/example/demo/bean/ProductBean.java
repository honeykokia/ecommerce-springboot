package com.example.demo.bean;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "promotion_id")
    private Long promotionId;
    
    @Column(name = "category_id", nullable = false)
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
    
    @Column(name = "short_description")
    private String shortDescription;
    
    // Foreign key relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", insertable = false, updatable = false)
    private PromotionBean promotion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryBean category;
}