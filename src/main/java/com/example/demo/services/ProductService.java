package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.ProductBean;
import com.example.demo.bean.PromotionBean;
import com.example.demo.bean.TagBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.ProductInfo;
import com.example.demo.dto.TagInfo;
import com.example.demo.enums.DiscountType;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductTagRepository;
import com.example.demo.responses.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductTagRepository productTagRepository;

    @Transactional
    public ApiResponse getProducts(String name, Long categoryId) {
        List<ProductBean> products = productRepository.findProductsWithFilters(name, categoryId);
        // products = calculateDiscountedPrices(products);
        List<ProductInfo> productInfoList = products.stream()
                .map(this::convertToProductInfo)
                .collect(Collectors.toList());
        
        return new ApiResponse(Map.of("products", productInfoList));
    }

    public ApiResponse getProduct(Long productId) {
        Optional<ProductBean> productOpt = productRepository.findProductById(productId);
        
        if (productOpt.isEmpty()) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("product", "Product not found");
            throw new ApiException("Product not found", 404, errorInfo);
        }
        
        ProductBean product = productOpt.get();
        ProductInfo productInfo = convertToProductInfo(product);
        
        return new ApiResponse(productInfo);
    }

    public ProductInfo convertToProductInfo(ProductBean product) {

        Integer discountedPrice = calculateDiscountPrice(product);

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());
        productInfo.setName(product.getName());
        productInfo.setOriginalPrice(product.getPrice());
        productInfo.setFinalPrice(discountedPrice);
        productInfo.setImageURL(product.getImageURL());
        productInfo.setInStock(product.getInStock());
        productInfo.setRating(product.getRating());
        productInfo.setSoldCount(product.getSoldCount());
        productInfo.setShortDescription(product.getShortDescription());

        if (product.getCategory() != null){
            productInfo.setCategoryId(product.getCategory().getId());
        }
        if (product.getPromotion() != null) {
            productInfo.setPromotionId(product.getPromotion().getId());
        }
        // Get tags for this product
        List<TagBean> tags = productTagRepository.findTagsByProductId(product.getId());
        List<TagInfo> tagInfos = tags.stream()
                .map(this::convertToTagInfo)
                .collect(Collectors.toList());
        productInfo.setTags(tagInfos);
        
        return productInfo;
    }
    
    private TagInfo convertToTagInfo(TagBean tag) {
        TagInfo tagInfo = new TagInfo();
        tagInfo.setId(tag.getId());
        tagInfo.setName(tag.getName());
        return tagInfo;
    }

    private Integer calculateDiscountPrice(ProductBean product) {

        Integer discount = 0;
        if (product.getPromotion() != null) {
            PromotionBean promotion = product.getPromotion();
            if (promotion.getDiscountType().equals(DiscountType.PERCENTAGE)) {
                discount = product.getPrice() * promotion.getDiscountValue() / 100;
            } else if (promotion.getDiscountType().equals(DiscountType.FIXED)) {
                discount = promotion.getDiscountValue();
            }
        }
        return product.getPrice() - discount;
    }
}