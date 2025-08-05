package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.ProductTagBean;
import com.example.demo.bean.TagBean;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTagBean, ProductTagBean.ProductTagId> {
    
    // Find all tags for a specific product
    @Query("SELECT pt.tag FROM ProductTagBean pt WHERE pt.productId = :productId")
    List<TagBean> findTagsByProductId(@Param("productId") Long productId);
    
    // Find all product tags for a specific product
    List<ProductTagBean> findByProductId(Long productId);
}