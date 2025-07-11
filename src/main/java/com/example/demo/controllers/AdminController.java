package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderInfo;
import com.example.demo.dto.ProductInfo;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.responses.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<UserInfo> users = new ArrayList<>();
        
        UserInfo user1 = new UserInfo();
        user1.setName("User1");
        user1.setEmail("user1@gmail.com");
        
        UserInfo user2 = new UserInfo();
        user2.setName("User2");
        user2.setEmail("user2@gmail.com");
        
        users.add(user1);
        users.add(user2);
        
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        
        ApiResponse response = new ApiResponse(data);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId, @RequestBody Map<String, String> statusRequest) {
        log.info("Updating user {} status to {}", userId, statusRequest.get("status"));
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductInfo productInfo) {
        log.info("Creating product: {}", productInfo.getName());
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductInfo productInfo) {
        log.info("Updating product {} with data: {}", productId, productInfo.getName());
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        log.info("Deleting product: {}", productId);
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        List<OrderInfo> orders = new ArrayList<>();
        
        OrderInfo order1 = new OrderInfo();
        order1.setId(1L);
        order1.setUserId(1L);
        order1.setOrderNumber("ORD123456789");
        order1.setStatus("PENDING");
        order1.setPaymentMethod("CREDIT_CARD");
        order1.setIsPaid(false);
        order1.setShippingAddress("台北市中正區某某路123號");
        order1.setShippingStatus("PENDING");
        order1.setTotalPrice(4000L);
        order1.setCreatedAt(LocalDateTime.now());
        order1.setUpdatedAt(LocalDateTime.now());
        
        OrderInfo order2 = new OrderInfo();
        order2.setId(2L);
        order2.setUserId(2L);
        order2.setOrderNumber("ORD987654321");
        order2.setStatus("PAID");
        order2.setPaymentMethod("PAYPAL");
        order2.setIsPaid(true);
        order2.setPaidAt(LocalDateTime.now());
        order2.setShippingAddress("高雄市前鎮區某某路456號");
        order2.setShippingStatus("SHIPPED");
        order2.setTotalPrice(2500L);
        order2.setCreatedAt(LocalDateTime.now());
        order2.setUpdatedAt(LocalDateTime.now());
        
        orders.add(order1);
        orders.add(order2);
        
        Map<String, Object> data = new HashMap<>();
        data.put("orders", orders);
        
        ApiResponse response = new ApiResponse(data);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderRequest updateOrderRequest) {
        log.info("Updating order {} status to {} with shipping method {} and address {}", 
                orderId, updateOrderRequest.getStatus(), updateOrderRequest.getShippingMethod(), updateOrderRequest.getShippingAddress());
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        log.info("Deleting order: {}", orderId);
        
        ApiResponse response = new ApiResponse(null);
        return ResponseEntity.ok().body(response);
    }
}