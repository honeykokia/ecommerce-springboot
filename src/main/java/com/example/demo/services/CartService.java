package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.CartBean;
import com.example.demo.bean.ProductBean;
import com.example.demo.dto.CartInfo;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.UpdateCartProductRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.responses.ApiResponse;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all cart items for a user
     */
    public ApiResponse getUserCart(Long userId) {
        List<CartBean> cartItems = cartRepository.findByUserId(userId);
        
        List<CartInfo> cartInfoList = cartItems.stream()
                .map(this::convertToCartInfo)
                .collect(Collectors.toList());
        
        return new ApiResponse(Map.of("cart", cartInfoList));
    }

    /**
     * Add product to cart or update quantity if already exists
     */
    @Transactional
    public ApiResponse addProductToCart(Long userId, CartInfo cartInfo) {
        // Validate product exists
        Optional<ProductBean> productOpt = productRepository.findById(cartInfo.getProductId());
        if (productOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("product", "產品不存在或已下架");
            throw new ApiException("Product not found", 400, errorInfo);
        }
        
        ProductBean product = productOpt.get();
        
        // Validate quantity
        if (cartInfo.getQuantity() == null || cartInfo.getQuantity() <= 0) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("quantity", "數量必須大於0");
            throw new ApiException("Invalid quantity", 400, errorInfo);
        }
        
        // Check if product already in cart
        Optional<CartBean> existingCartItem = cartRepository.findByUserIdAndProductId(userId, cartInfo.getProductId());
        
        CartBean cartBean;
        if (existingCartItem.isPresent()) {
            // Update existing cart item quantity
            cartBean = existingCartItem.get();
            cartBean.setQuantity(cartBean.getQuantity() + cartInfo.getQuantity());
            cartBean.setUpdatedAt(LocalDateTime.now());
        } else {
            // Create new cart item
            cartBean = new CartBean();
            cartBean.setUserId(userId);
            cartBean.setProductId(cartInfo.getProductId());
            cartBean.setQuantity(cartInfo.getQuantity());
            cartBean.setUnitPrice(product.getPrice());
            cartBean.setCreatedAt(LocalDateTime.now());
            cartBean.setUpdatedAt(LocalDateTime.now());
        }
        
        cartRepository.save(cartBean);
        
        return new ApiResponse(Map.of());
    }

    /**
     * Clear all cart items for a user
     */
    @Transactional
    public ApiResponse clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
        return new ApiResponse(Map.of());
    }

    /**
     * Remove specific product from cart
     */
    @Transactional
    public ApiResponse removeProductFromCart(Long userId, Long productId) {
        // Check if product exists in cart
        if (!cartRepository.existsByUserIdAndProductId(userId, productId)) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("productId", "產品不存在購物車中");
            throw new ApiException("Product not in cart", 400, errorInfo);
        }
        
        cartRepository.deleteByUserIdAndProductId(userId, productId);
        return new ApiResponse(Map.of());
    }

    /**
     * Update product quantity in cart
     */
    @Transactional
    public ApiResponse updateCartProductQuantity(Long userId, Long productId, UpdateCartProductRequest request) {
        // Validate quantity
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("quantity", "數量必須大於0");
            throw new ApiException("Invalid quantity", 400, errorInfo);
        }
        
        // Find cart item
        Optional<CartBean> cartItemOpt = cartRepository.findByUserIdAndProductId(userId, productId);
        if (cartItemOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("productId", "產品不存在購物車中");
            throw new ApiException("Product not in cart", 400, errorInfo);
        }
        
        CartBean cartBean = cartItemOpt.get();
        cartBean.setQuantity(request.getQuantity());
        cartBean.setUpdatedAt(LocalDateTime.now());
        
        cartRepository.save(cartBean);
        
        return new ApiResponse(Map.of());
    }

    /**
     * Convert CartBean to CartInfo DTO
     */
    private CartInfo convertToCartInfo(CartBean cartBean) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setId(cartBean.getId());
        cartInfo.setUserId(cartBean.getUserId());
        cartInfo.setProductId(cartBean.getProductId());
        cartInfo.setQuantity(cartBean.getQuantity());
        cartInfo.setUnitPrice(cartBean.getUnitPrice());
        cartInfo.setCreateAt(cartBean.getCreatedAt());
        cartInfo.setUpdateAt(cartBean.getUpdatedAt());
        return cartInfo;
    }
}