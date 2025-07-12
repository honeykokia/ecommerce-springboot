package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.ProductInfo;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.dto.UpdateUserStatusRequest;
import com.example.demo.responses.ApiResponse;

class AdminControllerTest {

    private AdminController adminController;

    @BeforeEach
    void setUp() {
        adminController = new AdminController();
    }

    @Test
    void testGetAllUsers() {
        ResponseEntity<?> response = adminController.getAllUsers();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testUpdateUserStatus() {
        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus("ACTIVE");
        
        ResponseEntity<?> response = adminController.updateUserStatus(1L, request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testCreateProduct() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("Test Product");
        productInfo.setPrice(1000);
        
        ResponseEntity<?> response = adminController.createProduct(productInfo);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testUpdateProduct() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("Updated Product");
        productInfo.setPrice(1500);
        
        ResponseEntity<?> response = adminController.updateProduct(1L, productInfo);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testDeleteProduct() {
        ResponseEntity<?> response = adminController.deleteProduct(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testGetAllOrders() {
        ResponseEntity<?> response = adminController.getAllOrders();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testUpdateOrderStatus() {
        UpdateOrderRequest request = new UpdateOrderRequest();
        request.setStatus("SHIPPED");
        request.setShippingMethod("EXPRESS");
        request.setShippingAddress("New Address");
        
        ResponseEntity<?> response = adminController.updateOrderStatus(1L, request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }

    @Test
    void testDeleteOrder() {
        ResponseEntity<?> response = adminController.deleteOrder(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
    }
}