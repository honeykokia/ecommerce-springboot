package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CheckoutRequest;
import com.example.demo.dto.OrderDetailInfo;
import com.example.demo.dto.OrderInfo;
import com.example.demo.responses.ApiResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @GetMapping("/me")
    public ResponseEntity<?> getOrders() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(1L);
        orderInfo.setUserId(1L);
        orderInfo.setOrderNumber("ORD123456789");
        orderInfo.setStatus("PENDING");
        orderInfo.setPaymentMethod("CREDIT_CARD");
        orderInfo.setIsPaid(false);
        orderInfo.setShippingAddress("台北市中正區某某路123號");
        orderInfo.setShippingStatus("PENDING");
        orderInfo.setTotalPrice(4000L);
        orderInfo.setCreatedAt(java.time.LocalDateTime.now());
        orderInfo.setUpdatedAt(java.time.LocalDateTime.now());
        ApiResponse response = new ApiResponse(Map.of("order", List.of(orderInfo)));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/me")
    public ResponseEntity<?> createOrder(@RequestBody CheckoutRequest checkoutRequest) {

        
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(1L);
        orderInfo.setUserId(1L);
        orderInfo.setOrderNumber("ORD123456789");
        orderInfo.setStatus("PENDING");
        orderInfo.setPaymentMethod("CREDIT_CARD");
        orderInfo.setIsPaid(false);
        orderInfo.setShippingAddress("台北市中正區某某路123號");
        orderInfo.setShippingStatus("PENDING");
        orderInfo.setTotalPrice(4000L);
        orderInfo.setCreatedAt(java.time.LocalDateTime.now());
        orderInfo.setUpdatedAt(java.time.LocalDateTime.now());
        ApiResponse response = new ApiResponse(Map.of("orders", orderInfo));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{orderId}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {

        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        orderDetailInfo.setOrderId(1L);
        orderDetailInfo.setProductId(1L);
        orderDetailInfo.setProductName("商品名稱");
        orderDetailInfo.setPrice(2000L);
        orderDetailInfo.setQuantity(2);
        orderDetailInfo.setTotalPrice(4000L);

        ApiResponse response = new ApiResponse(Map.of("items", List.of(orderDetailInfo)));
        return ResponseEntity.ok(response);
    }
    
}
