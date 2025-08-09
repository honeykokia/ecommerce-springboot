package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.bean.CartBean;
import com.example.demo.bean.ProductBean;
import com.example.demo.dto.CartInfo;
import com.example.demo.dto.UpdateCartProductRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.responses.ApiResponse;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private CartBean cartBean;
    private ProductBean productBean;
    private CartInfo cartInfo;

    @BeforeEach
    void setUp() {
        productBean = new ProductBean();
        productBean.setId(1L);
        productBean.setName("Test Product");
        productBean.setPrice(1000);

        cartBean = new CartBean();
        cartBean.setId(1L);
        cartBean.setUserId(1L);
        cartBean.setProductId(1L);
        cartBean.setQuantity(2);
        cartBean.setUnitPrice(1000);
        cartBean.setCreatedAt(LocalDateTime.now());
        cartBean.setUpdatedAt(LocalDateTime.now());

        cartInfo = new CartInfo();
        cartInfo.setProductId(1L);
        cartInfo.setQuantity(2);
    }

    @Test
    void testGetUserCart_Success() {
        // Arrange
        Long userId = 1L;
        List<CartBean> cartItems = Arrays.asList(cartBean);
        when(cartRepository.findByUserId(userId)).thenReturn(cartItems);

        // Act
        ApiResponse response = cartService.getUserCart(userId);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getData());
        @SuppressWarnings("unchecked")
        var dataMap = (java.util.Map<String, Object>) response.getData();
        @SuppressWarnings("unchecked")
        List<CartInfo> cart = (List<CartInfo>) dataMap.get("cart");
        assertEquals(1, cart.size());
        assertEquals(cartBean.getId(), cart.get(0).getId());
        assertEquals(cartBean.getUserId(), cart.get(0).getUserId());
        assertEquals(cartBean.getProductId(), cart.get(0).getProductId());
    }

    @Test
    void testAddProductToCart_ProductNotFound() {
        // Arrange
        Long userId = 1L;
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, 
            () -> cartService.addProductToCart(userId, cartInfo));
        
        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getData().toString().contains("產品不存在或已下架"));
    }

    @Test
    void testAddProductToCart_InvalidQuantity() {
        // Arrange
        Long userId = 1L;
        cartInfo.setQuantity(0); // Invalid quantity
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productBean));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, 
            () -> cartService.addProductToCart(userId, cartInfo));
        
        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getData().toString().contains("數量必須大於0"));
    }

    @Test
    void testAddProductToCart_NewProduct_Success() {
        // Arrange
        Long userId = 1L;
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productBean));
        when(cartRepository.findByUserIdAndProductId(userId, cartInfo.getProductId()))
            .thenReturn(Optional.empty());
        when(cartRepository.save(any(CartBean.class))).thenReturn(cartBean);

        // Act
        ApiResponse response = cartService.addProductToCart(userId, cartInfo);

        // Assert
        assertNotNull(response);
        verify(cartRepository).save(any(CartBean.class));
    }

    @Test
    void testUpdateCartProductQuantity_Success() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        UpdateCartProductRequest request = new UpdateCartProductRequest();
        request.setQuantity(5);
        
        when(cartRepository.findByUserIdAndProductId(userId, productId))
            .thenReturn(Optional.of(cartBean));
        when(cartRepository.save(any(CartBean.class))).thenReturn(cartBean);

        // Act
        ApiResponse response = cartService.updateCartProductQuantity(userId, productId, request);

        // Assert
        assertNotNull(response);
        verify(cartRepository).save(any(CartBean.class));
    }

    @Test
    void testUpdateCartProductQuantity_ProductNotInCart() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        UpdateCartProductRequest request = new UpdateCartProductRequest();
        request.setQuantity(5);
        
        when(cartRepository.findByUserIdAndProductId(userId, productId))
            .thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, 
            () -> cartService.updateCartProductQuantity(userId, productId, request));
        
        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getData().toString().contains("產品不存在購物車中"));
    }
}