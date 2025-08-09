package com.example.demo.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.ProductInfo;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.responses.ApiResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AdminRepository adminRepository;

    @Test
    public void testCreateProduct() {
        // Given
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("Test Product");
        productInfo.setPrice(1000);

        // When
        ApiResponse response = adminService.createProduct(productInfo);

        // Then
        assertNotNull(response);
        assertNotNull(response.getData());
    }
}