package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CartInfo;
import com.example.demo.dto.UpdateCartProductRequest;
import com.example.demo.responses.ApiResponse;

import java.util.HashMap;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 34579b3 (feat: add CartController APIs, DTOs, Request and correct api-spec (#10))
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/carts")
public class CartController {

    @GetMapping("/me")
    public ResponseEntity<?> getCarts() {

        CartInfo cartInfo = new CartInfo();
        cartInfo.setId(1L);
        cartInfo.setUserId(1L);
        cartInfo.setQuantity(2);
        cartInfo.setPrice(2000);
        cartInfo.setCreatedAt(java.time.LocalDateTime.now());
        cartInfo.setUpdatedAt(java.time.LocalDateTime.now());
        ApiResponse response = new ApiResponse(Map.of("cart",List.of(cartInfo)));
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/me")
    public ResponseEntity<?> addProductToCart(@RequestBody CartInfo cartInfo) {
        
        ApiResponse resopnse = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok(resopnse);
    }
    
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteCart() {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/{productId}")
    public ResponseEntity<?> deleteCartProduct(@PathVariable Long productId) {
        // Logic to delete the cart product
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/{productId}")
    public ResponseEntity<?> updateCartProduct(@PathVariable Long productId, @RequestBody UpdateCartProductRequest request) {
        // Logic to update the cart product
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok(response);
    }



}
