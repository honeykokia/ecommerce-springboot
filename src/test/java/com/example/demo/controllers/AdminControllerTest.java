package com.example.demo.controllers;

import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.dto.ProductInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users").isArray())
                .andExpect(jsonPath("$.data.users[0].name").value("User1"))
                .andExpect(jsonPath("$.data.users[0].email").value("user1@gmail.com"));
    }

    @Test
    void updateUserStatus() throws Exception {
        String statusRequest = "{\"status\":\"ACTIVE\"}";
        
        mockMvc.perform(patch("/admin/users/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(statusRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void createProduct() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("Test Product");
        productInfo.setPrice(1000);
        productInfo.setCategoryId(1L);
        
        mockMvc.perform(post("/admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void updateProduct() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("Updated Product");
        productInfo.setPrice(1500);
        productInfo.setCategoryId(1L);
        
        mockMvc.perform(put("/admin/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/admin/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void getAllOrders() throws Exception {
        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orders").isArray())
                .andExpect(jsonPath("$.data.orders[0].orderNumber").value("ORD123456789"))
                .andExpect(jsonPath("$.data.orders[0].status").value("PENDING"));
    }

    @Test
    void updateOrderStatus() throws Exception {
        UpdateOrderRequest updateRequest = new UpdateOrderRequest();
        updateRequest.setStatus("SHIPPED");
        updateRequest.setShippingMethod("EXPRESS");
        updateRequest.setShippingAddress("New Address");
        
        mockMvc.perform(patch("/admin/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void deleteOrder() throws Exception {
        mockMvc.perform(delete("/admin/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}