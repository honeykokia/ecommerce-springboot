package com.example.demo.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagBean {
    
    @EmbeddedId
    private ProductTagId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductBean product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private TagBean tag;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductTagId implements java.io.Serializable {
        private Long productId;
        private Long tagId;
    }
}