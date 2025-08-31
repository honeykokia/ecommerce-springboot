package com.example.demo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.responses.ApiResponse;
import com.example.demo.services.AdminService;

@WebMvcTest(value = AdminController.class, properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"})
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AdminService adminService;

    @Test
    public void testGetUsers() throws Exception {
        ApiResponse mockResponse = new ApiResponse(Map.of("users", List.of()));
        when(adminService.getAllUsers()).thenReturn(mockResponse);
        
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users").isArray());
    }

    @Test
    public void testCreateProduct() throws Exception {
        ApiResponse mockResponse = new ApiResponse(Map.of("message", "Product created successfully"));
        when(adminService.createProduct(any())).thenReturn(mockResponse);
        
        String requestBody = "{\"name\":\"Test Product\",\"price\":1000}";
        
        mockMvc.perform(post("/admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrders() throws Exception {
        ApiResponse mockResponse = new ApiResponse(Map.of("orders", List.of()));
        when(adminService.getAllOrders()).thenReturn(mockResponse);
        
        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orders").isArray());
    }
}