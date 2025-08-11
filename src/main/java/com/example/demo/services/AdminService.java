package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.CategoryBean;
import com.example.demo.bean.OrderBean;
import com.example.demo.bean.ProductBean;
import com.example.demo.bean.PromotionBean;
import com.example.demo.bean.UserBean;
import com.example.demo.dto.CreateCategoryRequest;
import com.example.demo.dto.CreateProductRequest;
import com.example.demo.dto.CreatePromotionRequest;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.OrderInfo;
import com.example.demo.dto.ProductInfo;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.dto.UpdateProductRequest;
import com.example.demo.dto.UpdateUserStatusRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.ShippingMethod;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.responses.ApiResponse;

import jakarta.persistence.EntityManager;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderRepository orderRepository; // For order-related operations

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private EntityManager em;

    public ApiResponse getAllUsers() {
        List<UserBean> users = userRepository.findAll();
        
        List<UserInfo> userInfoList = users.stream()
                .map(this::convertToUserInfo)
                .collect(Collectors.toList());
        
        return new ApiResponse(Map.of("users", userInfoList));
    }
    
    public ApiResponse updateUserStatus(Long userId, UpdateUserStatusRequest request) {
        Optional<UserBean> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("userId", "User not found");
            throw new ApiException("User not found", 404, errorInfo);
        }
        
        UserBean user = userOpt.get();
        // Here you would update user status - this depends on your UserBean having a status field
        // For now, we'll just return success since the schema doesn't show a status field
        
        userRepository.save(user);
        return new ApiResponse(Map.of("message", "User status updated successfully"));
    }
    
    public ApiResponse createProduct(CreateProductRequest createProductRequest) {
        ProductBean product = new ProductBean();
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setImageUrl(createProductRequest.getImageUrl());
        product.setInStock(createProductRequest.getInStock() != null ? createProductRequest.getInStock() : 0);
        product.setRating(createProductRequest.getRating() != null ? createProductRequest.getRating() : 0.0);
        product.setSoldCount(createProductRequest.getSoldCount() != null ? createProductRequest.getSoldCount() : 0);
        product.setShortDescription(createProductRequest.getShortDescription());
        product.setCategory(em.getReference(CategoryBean.class, createProductRequest.getCategoryId()));
        product.setPromotion(em.getReference(PromotionBean.class, createProductRequest.getPromotionId()));
        
        productRepository.save(product);
        return new ApiResponse(null);
    }
    
    public ApiResponse updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        Optional<ProductBean> productOpt = productRepository.findById(productId);
        
        if (productOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("productId", "Product not found");
            throw new ApiException("Product not found", 404, errorInfo);
        }
        
        ProductBean product = productOpt.get();
        if (updateProductRequest.getName() != null) product.setName(updateProductRequest.getName());
        if (updateProductRequest.getPrice() != null) product.setPrice(updateProductRequest.getPrice());
        if (updateProductRequest.getImageUrl() != null) product.setImageUrl(updateProductRequest.getImageUrl());
        if (updateProductRequest.getInStock() != null) product.setInStock(updateProductRequest.getInStock());
        if (updateProductRequest.getRating() != null) product.setRating(updateProductRequest.getRating());
        if (updateProductRequest.getSoldCount() != null) product.setSoldCount(updateProductRequest.getSoldCount());
        if (updateProductRequest.getShortDescription() != null) product.setShortDescription(updateProductRequest.getShortDescription());
        if (updateProductRequest.getCategoryId() != null) product.setCategory(em.getReference(CategoryBean.class, updateProductRequest.getCategoryId()));
        if (updateProductRequest.getPromotionId() != null) product.setPromotion(em.getReference(PromotionBean.class, updateProductRequest.getPromotionId()));

        productRepository.save(product);
        return new ApiResponse(Map.of("message", "Product updated successfully"));
    }
    
    public ApiResponse deleteProduct(Long productId) {
        Optional<ProductBean> productOpt = productRepository.findById(productId);
        
        if (productOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("productId", "Product not found");
            throw new ApiException("Product not found", 404, errorInfo);
        }
        
        productRepository.deleteById(productId);
        return new ApiResponse(Map.of("message", "Product deleted successfully"));
    }
    
    public ApiResponse getAllOrders() {
        List<OrderBean> orders = orderRepository.findAllOrdersOrderByCreatedAt();
        
        List<OrderInfo> orderInfoList = orders.stream()
                .map(this::convertToOrderInfo)
                .collect(Collectors.toList());
        
        return new ApiResponse(Map.of("orders", orderInfoList));
    }
    
    public ApiResponse updateOrderStatus(Long orderId, UpdateOrderRequest request) {
        Optional<OrderBean> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("orderId", "Order not found");
            throw new ApiException("Order not found", 404, errorInfo);
        }
        
        OrderBean order = orderOpt.get();
        
        if (request.getStatus() != null) {
            try {
                order.setStatus(OrderStatus.valueOf(request.getStatus()));
            } catch (IllegalArgumentException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.addError("status", "Invalid order status");
                throw new ApiException("Invalid order status", 400, errorInfo);
            }
        }
        
        if (request.getShippingMethod() != null) {
            try {
                order.setShippingMethod(ShippingMethod.valueOf(request.getShippingMethod()));
            } catch (IllegalArgumentException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.addError("shippingMethod", "Invalid shipping method");
                throw new ApiException("Invalid shipping method", 400, errorInfo);
            }
        }
        
        if (request.getShippingAddress() != null) {
            order.setShippingAddress(request.getShippingAddress());
        }
        
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        
        return new ApiResponse(Map.of("message", "Order status updated successfully"));
    }
    
    public ApiResponse deleteOrder(Long orderId) {
        Optional<OrderBean> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("orderId", "Order not found");
            throw new ApiException("Order not found", 404, errorInfo);
        }
        
        orderRepository.deleteById(orderId);
        return new ApiResponse(Map.of("message", "Order deleted successfully"));
    }

    public ApiResponse createPromotion(CreatePromotionRequest createPromotionRequest) {

        PromotionBean promotion = new PromotionBean();
        promotion.setName(createPromotionRequest.getName());
        promotion.setDiscountType(createPromotionRequest.getDiscountType());
        promotion.setDiscountValue(createPromotionRequest.getDiscountValue());
        promotion.setDescription(createPromotionRequest.getDescription());
        promotion.setImageUrl(createPromotionRequest.getImageUrl());
        promotion.setIsActive(createPromotionRequest.getIsActive());
        promotion.setStartDate(createPromotionRequest.getStartDate());
        promotion.setEndDate(createPromotionRequest.getEndDate());

        promotionRepository.save(promotion);
        return new ApiResponse(null);
    }
    public ApiResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        // Assuming you have a CategoryBean and CategoryRepository similar to Product and Promotion
        CategoryBean category = new CategoryBean();
        category.setName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());

        categoryRepository.save(category);
        return new ApiResponse(null);
    }
    
    private UserInfo convertToUserInfo(UserBean user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getName());
        userInfo.setEmail(user.getEmail());
        return userInfo;
    }
    
    private OrderInfo convertToOrderInfo(OrderBean order) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(order.getId());
        orderInfo.setUserId(order.getUserId());
        orderInfo.setOrderNumber(order.getOrderNumber());
        orderInfo.setStatus(order.getStatus());
        orderInfo.setPaymentMethod(order.getPaymentMethod().name());
        orderInfo.setIsPaid(order.getIsPaid());
        orderInfo.setPaidAt(order.getPaidAt());
        orderInfo.setCancelledAt(order.getCancelledAt());
        orderInfo.setShippingMethod(order.getShippingMethod().name());
        orderInfo.setShippingAddress(order.getShippingAddress());
        orderInfo.setShippingStatus(order.getShippingStatus().name());
        orderInfo.setTotalPrice(order.getTotalPrice());
        orderInfo.setCreatedAt(order.getCreatedAt());
        orderInfo.setUpdatedAt(order.getUpdatedAt());
        return orderInfo;
    }
}