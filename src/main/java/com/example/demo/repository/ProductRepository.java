package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.ProductBean;

@Repository
public interface ProductRepository extends JpaRepository<ProductBean, Long> {
    
    // Find products by name containing (case insensitive)
    List<ProductBean> findByNameContainingIgnoreCase(String name);
    
    // Find products by category ID
    List<ProductBean> findByCategoryId(Long categoryId);
    
    // Find products by name and category ID
    List<ProductBean> findByNameContainingIgnoreCaseAndCategoryId(String name, Long categoryId);
    
    // Custom query to find products with tags
    @Query("SELECT DISTINCT p FROM ProductBean p " +
           "LEFT JOIN ProductTagBean pt ON pt.productId = p.id " +
           "LEFT JOIN TagBean t ON t.id = pt.tagId " +
           "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:categoryId IS NULL OR p.categoryId = :categoryId)")
    List<ProductBean> findProductsWithFilters(@Param("name") String name, @Param("categoryId") Long categoryId);
    
    // Find a product with its tags
    @Query("SELECT p FROM ProductBean p WHERE p.id = :productId")
    Optional<ProductBean> findProductById(@Param("productId") Long productId);

    @Query("SELECT p.price FROM ProductBean p WHERE p.id = :productId")
    Integer getPrice(@Param("productId") Long productId);
}