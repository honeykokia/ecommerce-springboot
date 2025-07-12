package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UpdateUserStatusRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.dto.ProductInfo;
import com.example.demo.dto.CategoryInfo;
import com.example.demo.dto.OrderInfo;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.responses.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@gmail.com");
        userInfo.setName("Test");

        ApiResponse response = new ApiResponse(Map.of("users", List.of(userInfo)));

        return ResponseEntity.ok().body(response);
    }
    
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId, @RequestBody UpdateUserStatusRequest updateUserStatusRequest) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductInfo productInfo) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductInfo productInfo) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(1L);
        orderInfo.setUserId(1L);
        orderInfo.setOrderNumber("ORD123456789");
        orderInfo.setStatus("PENDING");
        orderInfo.setPaymentMethod("CREDIT_CARD");
        orderInfo.setIsPaid(false);
        orderInfo.setShippingMethod("STANDARD");
        orderInfo.setShippingAddress("台北市中正區某某路123號");
        orderInfo.setShippingStatus("PENDING");
        orderInfo.setTotalPrice(4000L);

        ApiResponse response = new ApiResponse(Map.of("orders", List.of(orderInfo)));
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderRequest updateOrderRequest) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryInfo categoryInfo) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryInfo categoryInfo) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        ApiResponse response = new ApiResponse(new HashMap<>());
        return ResponseEntity.ok().body(response);
    }
}