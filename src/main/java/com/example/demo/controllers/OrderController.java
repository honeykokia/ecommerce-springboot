package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.responses.ApiResponse;
import com.example.demo.services.OrderService;



@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @GetMapping("/me")
    public ResponseEntity<?> getOrders(@AuthenticationPrincipal Long userId) {
        ApiResponse response = orderService.getOrders(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/me")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal Long userId, @RequestBody CreateOrderRequest createOrderRequest) {

        ApiResponse response = orderService.createOrder(userId, createOrderRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<?> getOrderStatusById(@PathVariable Long orderId) {
        ApiResponse response = orderService.getOrderStatusById(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {

        ApiResponse response = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(response);
    }
    
}
