package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUsers() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users").isArray());
    }

    @Test
    public void testUpdateUserStatus() throws Exception {
        String requestBody = "{\"status\":\"ACTIVE\"}";
        
        mockMvc.perform(patch("/admin/users/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateProduct() throws Exception {
        String requestBody = "{\"name\":\"Test Product\",\"price\":1000}";
        
        mockMvc.perform(post("/admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        String requestBody = "{\"name\":\"Updated Product\",\"price\":1500}";
        
        mockMvc.perform(put("/admin/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/admin/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrders() throws Exception {
        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orders").isArray());
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        String requestBody = "{\"status\":\"PAID\",\"shippingMethod\":\"EXPRESS\",\"shippingAddress\":\"New Address\"}";
        
        mockMvc.perform(patch("/admin/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/admin/orders/1"))
                .andExpect(status().isOk());
    }
}