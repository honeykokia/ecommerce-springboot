package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.bean.ProductBean;
import com.example.demo.bean.TagBean;
import com.example.demo.dto.ProductInfo;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductTagRepository;
import com.example.demo.responses.ApiResponse;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private ProductTagRepository productTagRepository;
    
    @InjectMocks
    private ProductService productService;
    
    private ProductBean testProduct;
    private TagBean testTag;
    
    @BeforeEach
    void setUp() {
        testProduct = new ProductBean();
        testProduct.setId(1L);
        testProduct.setName("藍芽耳機");
        testProduct.setPrice(2000);
        testProduct.setImageUrl("/upload/defaultProduct.jpg");
        testProduct.setInStock(20);
        testProduct.setRating(4.5);
        testProduct.setSoldCount(36);
        testProduct.setShortDescription("這是一款高品質的藍芽耳機，提供卓越的音質和舒適的佩戴體驗。");
        testProduct.setCategoryId(1L);
        testProduct.setPromotionId(1L);
        
        testTag = new TagBean();
        testTag.setId(1L);
        testTag.setName("熱賣");
    }
    
    @Test
    void testGetProducts() {
        // Arrange
        List<ProductBean> products = Arrays.asList(testProduct);
        List<TagBean> tags = Arrays.asList(testTag);
        
        when(productRepository.findProductsWithFilters(any(), any())).thenReturn(products);
        when(productTagRepository.findTagsByProductId(1L)).thenReturn(tags);
        
        // Act
        ApiResponse response = productService.getProducts("藍芽", 1L);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getData());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> dataMap = (Map<String, Object>) response.getData();
        assertTrue(dataMap.containsKey("products"));
        
        @SuppressWarnings("unchecked")
        List<ProductInfo> productInfos = (List<ProductInfo>) dataMap.get("products");
        assertEquals(1, productInfos.size());
        
        ProductInfo productInfo = productInfos.get(0);
        assertEquals(testProduct.getId(), productInfo.getId());
        assertEquals(testProduct.getName(), productInfo.getName());
        assertEquals(testProduct.getPrice(), productInfo.getPrice());
        assertEquals(1, productInfo.getTags().size());
        assertEquals(testTag.getName(), productInfo.getTags().get(0).getName());
    }
    
    @Test
    void testGetProduct() {
        // Arrange
        List<TagBean> tags = Arrays.asList(testTag);
        
        when(productRepository.findProductById(1L)).thenReturn(Optional.of(testProduct));
        when(productTagRepository.findTagsByProductId(1L)).thenReturn(tags);
        
        // Act
        ApiResponse response = productService.getProduct(1L);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getData());
        
        ProductInfo productInfo = (ProductInfo) response.getData();
        assertEquals(testProduct.getId(), productInfo.getId());
        assertEquals(testProduct.getName(), productInfo.getName());
        assertEquals(testProduct.getPrice(), productInfo.getPrice());
        assertEquals(testProduct.getCategoryId(), productInfo.getCategoryId());
        assertEquals(testProduct.getPromotionId(), productInfo.getPromotionId());
        assertEquals(1, productInfo.getTags().size());
        assertEquals(testTag.getName(), productInfo.getTags().get(0).getName());
    }
    
    @Test
    void testGetProductNotFound() {
        // Arrange
        when(productRepository.findProductById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            productService.getProduct(999L);
        });
        
        assertEquals("Product not found", exception.getMessage());
        assertEquals(404, exception.getStatusCode());
    }
}