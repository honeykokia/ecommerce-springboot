package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CreateCategoryRequest;
import com.example.demo.dto.CreateProductRequest;
import com.example.demo.dto.CreatePromotionRequest;
import com.example.demo.dto.CreateTagRequest;
import com.example.demo.dto.UpdateCategoryRequest;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.dto.UpdateProductImageRequest;
import com.example.demo.dto.UpdateProductRequest;
import com.example.demo.dto.UpdatePromotionImageRequest;
import com.example.demo.dto.UpdatePromotionRequest;
import com.example.demo.dto.UpdateTagRequest;
import com.example.demo.dto.UpdateUserStatusRequest;
import com.example.demo.enums.UserRole;
import com.example.demo.exception.ApiException;
import com.example.demo.responses.ApiResponse;
import com.example.demo.services.AdminService;
import com.example.demo.vaildator.AccessGuard;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AccessGuard accessGuard;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal Long userId) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.getAllUsers();
        return ResponseEntity.ok().body(response);
    }
    
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
        @AuthenticationPrincipal Long callerId,
        @PathVariable Long userId,
        @RequestBody UpdateUserStatusRequest updateUserStatusRequest) {
        if (!accessGuard.hasRole(callerId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.updateUserStatus(userId, updateUserStatusRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal Long callerId, @RequestBody CreateProductRequest createProductRequest) {
        if (!accessGuard.hasRole(callerId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.createProduct(createProductRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(
        @AuthenticationPrincipal Long callerId,
        @PathVariable Long productId,
        @RequestBody UpdateProductRequest updateProductRequest) {
        if (!accessGuard.hasRole(callerId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }

        ApiResponse response = adminService.updateProduct(productId, updateProductRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<?> UpdateProductImage(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long productId,
        @RequestParam("file") MultipartFile file) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }

        UpdateProductImageRequest updateProductImageRequest = new UpdateProductImageRequest();
        updateProductImageRequest.setProductId(productId);
        updateProductImageRequest.setFile(file);
        ApiResponse response = adminService.updateProductImage(updateProductImageRequest);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal Long userId, @PathVariable Long productId) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.deleteProduct(productId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@AuthenticationPrincipal Long userId) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.getAllOrders();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<?> updateOrderStatus(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long orderId, 
        @RequestBody UpdateOrderRequest updateOrderRequest) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.updateOrderStatus(orderId, updateOrderRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@AuthenticationPrincipal Long userId , @PathVariable Long orderId) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.deleteOrder(orderId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/promotions")
    public ResponseEntity<?> createPromotion(@AuthenticationPrincipal Long userId, @RequestBody CreatePromotionRequest createPromotionRequest) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.createPromotion(createPromotionRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/promotions/{promotionId}")
    public ResponseEntity<?> updatePromotion(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long promotionId, 
        @RequestBody UpdatePromotionRequest updatePromotionRequest) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        updatePromotionRequest.setId(promotionId);
        ApiResponse response = adminService.updatePromotion(updatePromotionRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@AuthenticationPrincipal Long userId, @RequestBody CreateCategoryRequest createCategoryRequest) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.createCategory(createCategoryRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/promotions/{promotionId}")
    public ResponseEntity<?> deletePromotion(@AuthenticationPrincipal Long userId, @PathVariable Long promotionId) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.deletePromotion(promotionId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/promotions/{promotionId}/image")
    public ResponseEntity<?> updatePromotionImage(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long promotionId,
        @RequestParam("file") MultipartFile file) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        
        UpdatePromotionImageRequest updatePromotionImageRequest = new UpdatePromotionImageRequest();
        updatePromotionImageRequest.setPromotionId(promotionId);
        updatePromotionImageRequest.setFile(file);
        
        ApiResponse response = adminService.updatePromotionImage(updatePromotionImageRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long categoryId,
        @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.updateCategory(categoryId, updateCategoryRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@AuthenticationPrincipal Long userId, @PathVariable Long categoryId) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.deleteCategory(categoryId);
        return ResponseEntity.ok().body(response);
    }
    
    @GetMapping("/tags")
    public ResponseEntity<?> getTags(@AuthenticationPrincipal Long userId) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.getTags();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/tags")
    public ResponseEntity<?> createTag(@AuthenticationPrincipal Long userId, @RequestBody CreateTagRequest createTagRequest) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.createTag(createTagRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<?> updateTag(@AuthenticationPrincipal Long userId, @PathVariable Long tagId, @RequestBody UpdateTagRequest updateTagRequest) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.updateTag(tagId, updateTagRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<?> deleteTag(@AuthenticationPrincipal Long userId, @PathVariable Long tagId) {
        if (!accessGuard.hasRole(userId, UserRole.ADMIN)) {
            throw new ApiException("Access denied", 403, null);
        }
        ApiResponse response = adminService.deleteTag(tagId);
        return ResponseEntity.ok().body(response);
    }

}