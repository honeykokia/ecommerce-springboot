package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

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

import com.example.demo.dto.OrderInfo;
import com.example.demo.dto.ProductInfo;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.dto.UpdateUserStatusRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.responses.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        log.info("Admin - Getting all users");
        
        // Mock data for demonstration
        List<UserInfo> users = new ArrayList<>();
        UserInfo user1 = new UserInfo();
        user1.setName("Test User 1");
        user1.setEmail("user1@gmail.com");
        users.add(user1);
        
        UserInfo user2 = new UserInfo();
        user2.setName("Test User 2");
        user2.setEmail("user2@gmail.com");
        users.add(user2);
        
        ApiResponse response = new ApiResponse(users);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId, @RequestBody UpdateUserStatusRequest request) {
        log.info("Admin - Updating user {} status to {}", userId, request.getStatus());
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductInfo productInfo) {
        log.info("Admin - Creating new product: {}", productInfo.getName());
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductInfo productInfo) {
        log.info("Admin - Updating product {}: {}", productId, productInfo.getName());
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        log.info("Admin - Deleting product {}", productId);
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        log.info("Admin - Getting all orders");
        
        // Mock data for demonstration
        List<OrderInfo> orders = new ArrayList<>();
        OrderInfo order1 = new OrderInfo();
        order1.setId(1L);
        order1.setUserId(1L);
        order1.setOrderNumber("ORD123456789");
        order1.setStatus("PENDING");
        order1.setPaymentMethod("CREDIT_CARD");
        order1.setIsPaid(false);
        order1.setShippingMethod("STANDARD");
        order1.setShippingAddress("台北市中正區某某路123號");
        order1.setShippingStatus("PENDING");
        order1.setTotalPrice(4000L);
        orders.add(order1);
        
        ApiResponse response = new ApiResponse(orders);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderRequest request) {
        log.info("Admin - Updating order {} status to {}", orderId, request.getStatus());
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        log.info("Admin - Deleting order {}", orderId);
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }
}