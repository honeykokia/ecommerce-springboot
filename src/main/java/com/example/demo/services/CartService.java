package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.CartBean;
import com.example.demo.bean.CartItemBean;
import com.example.demo.bean.ProductBean;
import com.example.demo.bean.UserBean;
import com.example.demo.dto.CartAddItemRequest;
import com.example.demo.dto.CartInfo;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.UpdateCartProductRequest;
import com.example.demo.enums.CartStatus;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.responses.ApiResponse;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager em;

    public ApiResponse getUserCart(Long userId) {
        Optional<CartBean> cartOpt = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE);
        if (!cartOpt.isPresent()) {
            CartBean newCart = new CartBean();
            newCart.setUser(em.getReference(UserBean.class, userId));
            newCart.setStatus(CartStatus.ACTIVE);
            newCart.setCreatedAt(LocalDateTime.now());
            cartRepository.save(newCart);
            return new ApiResponse(Map.of("cart", List.of()));
        }

        List<CartItemBean> cartItems = cartItemRepository.findByCartId(cartOpt.get().getId());
        List<CartInfo> cartInfoList = cartItems.stream()
            .map(item -> {
                CartInfo info = new CartInfo();
                info.setProductId(item.getProduct().getId());
                info.setUserId(userId);
                info.setQuantity(item.getQuantity());
                info.setUnitPrice(item.getUnitPrice());
                info.setCreateAt(item.getCart().getCreatedAt());
                return info;
            })
            .toList();
        return new ApiResponse(Map.of("cart", cartInfoList));
    }

    @Transactional
    public ApiResponse addProductToCart(Long userId, CartAddItemRequest cartAddItemRequest) {
        CartBean cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .orElseGet(() -> {
                CartBean c = new CartBean();
                c.setUser(em.getReference(UserBean.class, userId));
                c.setStatus(CartStatus.ACTIVE);
                c.setCreatedAt(LocalDateTime.now());
                return cartRepository.save(c);
            });

        Optional<CartItemBean> existingItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), cartAddItemRequest.getProductId());
        if (existingItemOpt.isPresent()) {
            // Update quantity if item already exists
            CartItemBean existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartAddItemRequest.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            // Add new item to cart
            CartItemBean newItem = new CartItemBean();
            newItem.setProduct(em.getReference(ProductBean.class, cartAddItemRequest.getProductId()));
            newItem.setQuantity(cartAddItemRequest.getQuantity());
            newItem.setUnitPrice(productRepository.getPrice(cartAddItemRequest.getProductId()));
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        return new ApiResponse(Map.of());
    }

    @Transactional
    public ApiResponse clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
        return new ApiResponse(Map.of());
    }

    @Transactional
    public ApiResponse removeProductFromCart(Long userId, Long productId) {
        // Check if product exists in cart
        CartBean cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .orElseThrow(() -> {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.addError("cart", "cart not found");
                throw new ApiException("Cart not found", 400, errorInfo);
            });
        Optional<CartItemBean> cartItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if(!cartItemOpt.isPresent()){
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("productId", "product not in cart");
            throw new ApiException("Product not in cart", 400, errorInfo);
        }

        cart.removeItem(cartItemOpt.get());
        return new ApiResponse(Map.of());
    }

    @Transactional
    public ApiResponse updateCartProductQuantity(Long userId, Long productId, UpdateCartProductRequest request) {
        // Validate quantity

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("quantity", "quantity must be greater than 0");
            throw new ApiException("Invalid quantity", 400, errorInfo);
        }
        
        CartBean cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .orElseThrow(() -> {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.addError("cart", "cart not found");
                throw new ApiException("Cart not found", 400, errorInfo);
            });

        // Find cart item
        Optional<CartItemBean> cartItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (cartItemOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("productId", "product not in cart");
            throw new ApiException("Product not in cart", 400, errorInfo);
        }
        CartItemBean cartItemBean = cartItemOpt.get();
        cartItemBean.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItemBean);

        
        return new ApiResponse(Map.of());
    }

    // /**
    //  * Convert CartBean to CartInfo DTO
    //  */
}