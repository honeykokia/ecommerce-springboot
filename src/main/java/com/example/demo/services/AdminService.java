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
import com.example.demo.bean.TagBean;
import com.example.demo.bean.UserBean;
import com.example.demo.dto.CreateCategoryRequest;
import com.example.demo.dto.CreateProductRequest;
import com.example.demo.dto.CreatePromotionRequest;
import com.example.demo.dto.CreateTagRequest;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.OrderInfo;
import com.example.demo.dto.TagInfo;
import com.example.demo.dto.UpdateCategoryRequest;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.dto.UpdateProductImageRequest;
import com.example.demo.dto.UpdateProductRequest;
import com.example.demo.dto.UpdatePromotionImageRequest;
import com.example.demo.dto.UpdatePromotionRequest;
import com.example.demo.dto.UpdateTagRequest;
import com.example.demo.dto.UpdateUserStatusRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.ShippingMethod;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.responses.ApiResponse;
import com.example.demo.vaildator.UpdateProductImageValidator;
import com.example.demo.vaildator.UpdatePromotionImageValidator;
import com.example.demo.vaildator.UpdatePromotionValidator;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

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
    private TagRepository tagRepository;

    @Autowired
    private UpdatePromotionValidator updatePromotionValidator;

    @Autowired
    private EntityManager em;

    @Autowired
    private UpdatePromotionImageValidator updatePromotionImageValidator;

    @Autowired
    private UpdateProductImageValidator updateProductImageValidator;


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
        user.setStatus(request.getStatus());
        
        userRepository.save(user);
        return new ApiResponse(Map.of("message", "User status updated successfully"));
    }
    
    @Transactional
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

    @Transactional
    public ApiResponse updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        Optional<ProductBean> productOpt = productRepository.findById(productId);

        if (productOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("productId", "Product not found");
            throw new ApiException("Product not found", 404, errorInfo);
        }

        ProductBean product = productOpt.get();
        if (updateProductRequest.getName() != null) product.setName(updateProductRequest.getName());
        if (updateProductRequest.getOriginalPrice() != null) product.setPrice(updateProductRequest.getOriginalPrice());
        // if (updateProductRequest.getImageURL() != null) product.get().setImageUrl(updateProductRequest.getImageURL());
        if (updateProductRequest.getInStock() != null) product.setInStock(updateProductRequest.getInStock());
        if (updateProductRequest.getRating() != null) product.setRating(updateProductRequest.getRating());
        if (updateProductRequest.getSoldCount() != null) product.setSoldCount(updateProductRequest.getSoldCount());
        if (updateProductRequest.getShortDescription() != null) product.setShortDescription(updateProductRequest.getShortDescription());
        if (updateProductRequest.getCategoryId() != null) product.setCategory(em.getReference(CategoryBean.class, updateProductRequest.getCategoryId()));
        if (updateProductRequest.getPromotionId() != null) product.setPromotion(em.getReference(PromotionBean.class, updateProductRequest.getPromotionId()));

        productRepository.save(product);
        return new ApiResponse(Map.of("message", "Product updated successfully"));
    }
    
    @Transactional
    public ApiResponse updateProductImage(UpdateProductImageRequest request) {
        ProductBean product = updateProductImageValidator.validate(request);
        productRepository.save(product);
        return new ApiResponse(Map.of("message", "Product image updated successfully"));
    }


    @Transactional
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
    
    @Transactional
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

    @Transactional
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

    @Transactional
    public ApiResponse updatePromotion(UpdatePromotionRequest updatePromotionRequest) {
        PromotionBean promotionBean = updatePromotionValidator.validate(updatePromotionRequest);

        if (updatePromotionRequest.getName() != null) {
            promotionBean.setName(updatePromotionRequest.getName());
        }
        if (updatePromotionRequest.getDiscountType() != null) {
            promotionBean.setDiscountType(updatePromotionRequest.getDiscountType());
        }
        if (updatePromotionRequest.getDiscountValue() != null) {
            promotionBean.setDiscountValue(updatePromotionRequest.getDiscountValue());
        }
        if (updatePromotionRequest.getDescription() != null) {
            promotionBean.setDescription(updatePromotionRequest.getDescription());
        }
        if (updatePromotionRequest.getIsActive() != null) {
            promotionBean.setIsActive(updatePromotionRequest.getIsActive());
        }
        if (updatePromotionRequest.getStartDate() != null) {
            promotionBean.setStartDate(updatePromotionRequest.getStartDate());
        }
        if (updatePromotionRequest.getEndDate() != null) {
            promotionBean.setEndDate(updatePromotionRequest.getEndDate());
        }
        // promotionRepository.save(promotionBean);
        return new ApiResponse(null);
    }
    @Transactional
    public ApiResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        // Assuming you have a CategoryBean and CategoryRepository similar to Product and Promotion
        CategoryBean category = new CategoryBean();
        category.setName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());

        categoryRepository.save(category);
        return new ApiResponse(null);
    }

    @Transactional
    public ApiResponse deletePromotion(Long promotionId) {
        Optional<PromotionBean> promotionOpt = promotionRepository.findById(promotionId);
        
        if (promotionOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("promotionId", "Promotion not found");
            throw new ApiException("Promotion not found", 404, errorInfo);
        }
        
        promotionRepository.deleteById(promotionId);
        return new ApiResponse(Map.of("message", "Promotion deleted successfully"));
    }

    @Transactional
    public ApiResponse updatePromotionImage(UpdatePromotionImageRequest request) {
        PromotionBean promotion = updatePromotionImageValidator.validate(request);
        promotionRepository.save(promotion);
        return new ApiResponse(Map.of("message", "Promotion image updated successfully"));
    }

    @Transactional
    public ApiResponse updateCategory(Long categoryId, UpdateCategoryRequest updateCategoryRequest) {
        Optional<CategoryBean> categoryOpt = categoryRepository.findById(categoryId);
        
        if (categoryOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("categoryId", "Category not found");
            throw new ApiException("Category not found", 404, errorInfo);
        }
        
        CategoryBean category = categoryOpt.get();
        if (updateCategoryRequest.getName() != null) category.setName(updateCategoryRequest.getName());
        if (updateCategoryRequest.getDescription() != null) category.setDescription(updateCategoryRequest.getDescription());

        categoryRepository.save(category);
        return new ApiResponse(Map.of("message", "Category updated successfully"));
    }

    @Transactional
    public ApiResponse deleteCategory(Long categoryId) {
        Optional<CategoryBean> categoryOpt = categoryRepository.findById(categoryId);
        
        if (categoryOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("categoryId", "Category not found");
            throw new ApiException("Category not found", 404, errorInfo);
        }
        
        categoryRepository.deleteById(categoryId);
        return new ApiResponse(Map.of("message", "Category deleted successfully"));
    }

    @Transactional
    public ApiResponse getTags() {
        List<TagBean> tags = tagRepository.findAll();
        List<TagInfo> tagInfoList = tags.stream()
                .map(this::convertToTagInfo)
                .collect(Collectors.toList());
        return new ApiResponse(Map.of("tags", tagInfoList));
    }

    @Transactional
    public ApiResponse createTag(CreateTagRequest createTagRequest){
        TagBean tag = new TagBean();
        tag.setName(createTagRequest.getName());
        tag.setDescription(createTagRequest.getDescription());
        tag.setColor(createTagRequest.getColor());
        tagRepository.save(tag);
        return new ApiResponse(Map.of("message", "Tag created successfully"));
    }

    @Transactional
    public ApiResponse updateTag(Long tagId, UpdateTagRequest updateTagRequest) {

        Optional<TagBean> tagOpt = tagRepository.findById(tagId);
        if (tagOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("id", "Tag not found");
            throw new ApiException("Tag not found", 404, errorInfo);
        }

        TagBean tag = tagOpt.get();
        if (updateTagRequest.getName() != null) {
            tag.setName(updateTagRequest.getName());
        }
        if (updateTagRequest.getDescription() != null) {
            tag.setDescription(updateTagRequest.getDescription());
        }
        if (updateTagRequest.getColor() != null) {
            tag.setColor(updateTagRequest.getColor());
        }

        tagRepository.save(tag);
        return new ApiResponse(Map.of("message", "Tag updated successfully"));
    }

    public ApiResponse deleteTag(Long tagId) {
        Optional<TagBean> tagOpt = tagRepository.findById(tagId);
        if (tagOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("id", "Tag not found");
            throw new ApiException("Tag not found", 404, errorInfo);
        }
        tagRepository.deleteById(tagId);
        return new ApiResponse(Map.of("message", "Tag deleted successfully"));
    }

    private TagInfo convertToTagInfo(TagBean tag) {
        TagInfo tagInfo = new TagInfo();
        tagInfo.setId(tag.getId());
        tagInfo.setName(tag.getName());
        tagInfo.setDescription(tag.getDescription());
        tagInfo.setColor(tag.getColor());
        return tagInfo;
    }

    private UserInfo convertToUserInfo(UserBean user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setStatus(user.getStatus());
        userInfo.setRole(user.getRole());
        return userInfo;
    }
    
    private OrderInfo convertToOrderInfo(OrderBean order) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(order.getId());
        orderInfo.setUserId(order.getUser().getId());
        orderInfo.setMerchantTradeNo(order.getMerchantTradeNo());
        orderInfo.setStatus(order.getStatus());
        orderInfo.setPaymentMethod(order.getPaymentMethod());
        orderInfo.setPaidAt(order.getPaidAt());
        orderInfo.setCancelledAt(order.getCancelledAt());
        orderInfo.setShippingMethod(order.getShippingMethod());
        orderInfo.setShippingAddress(order.getShippingAddress());
        orderInfo.setShippingStatus(order.getShippingStatus());
        orderInfo.setAmountCents(order.getAmountCents());
        orderInfo.setCurrency(order.getCurrency());
        orderInfo.setCreatedAt(order.getCreatedAt());
        orderInfo.setUpdatedAt(order.getUpdatedAt());
        return orderInfo;
    }
}