package com.example.demo.bean;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "product_tags")
@IdClass(ProductTagBean.ProductTagId.class)
@Data
public class ProductTagBean {
    @Id
    @Column(name = "product_id")
    private Long productId;
    
    @Id
    @Column(name = "tag_id")
    private Long tagId;
    
    // Foreign key relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductBean product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private TagBean tag;
    
    // Composite key class
    @Data
    public static class ProductTagId implements Serializable {
        private Long productId;
        private Long tagId;
    }
}