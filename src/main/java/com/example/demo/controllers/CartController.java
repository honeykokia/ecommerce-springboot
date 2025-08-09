package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CartInfo;
import com.example.demo.dto.UpdateCartProductRequest;
import com.example.demo.responses.ApiResponse;
import com.example.demo.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCarts(@AuthenticationPrincipal Long userId) {
        ApiResponse response = cartService.getUserCart(userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/me")
    public ResponseEntity<ApiResponse> addProductToCart(
            @AuthenticationPrincipal Long userId,
            @RequestBody CartInfo cartInfo) {
        ApiResponse response = cartService.addProductToCart(userId, cartInfo);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse> deleteCart(@AuthenticationPrincipal Long userId) {
        ApiResponse response = cartService.clearCart(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/{productId}")
    public ResponseEntity<ApiResponse> deleteCartProduct(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId) {
        ApiResponse response = cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/{productId}")
    public ResponseEntity<ApiResponse> updateCartProduct(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId, 
            @RequestBody UpdateCartProductRequest request) {
        ApiResponse response = cartService.updateCartProductQuantity(userId, productId, request);
        return ResponseEntity.ok(response);
    }
}
