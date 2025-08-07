package com.example.demo.bean;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_tags")
@IdClass(ProductTagBean.ProductTagId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // optional
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