package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateCategoryRequest;
import com.example.demo.dto.CreatePromotionRequest;
import com.example.demo.dto.ProductInfo;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.dto.UpdateUserStatusRequest;
import com.example.demo.responses.ApiResponse;
import com.example.demo.services.AdminService;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        ApiResponse response = adminService.getAllUsers();
        return ResponseEntity.ok().body(response);
    }
    
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId, @RequestBody UpdateUserStatusRequest updateUserStatusRequest) {
        ApiResponse response = adminService.updateUserStatus(userId, updateUserStatusRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductInfo productInfo) {
        ApiResponse response = adminService.createProduct(productInfo);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductInfo productInfo) {
        ApiResponse response = adminService.updateProduct(productId, productInfo);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        ApiResponse response = adminService.deleteProduct(productId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        ApiResponse response = adminService.getAllOrders();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderRequest updateOrderRequest) {
        ApiResponse response = adminService.updateOrderStatus(orderId, updateOrderRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        ApiResponse response = adminService.deleteOrder(orderId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/promotions")
    public ResponseEntity<?> createPromotion(@RequestBody CreatePromotionRequest createPromotionRequest) {
        ApiResponse response = adminService.createPromotion(createPromotionRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        ApiResponse response = adminService.createCategory(createCategoryRequest);
        return ResponseEntity.ok().body(response);
    }
}