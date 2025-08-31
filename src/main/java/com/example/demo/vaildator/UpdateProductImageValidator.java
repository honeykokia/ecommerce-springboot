package com.example.demo.vaildator;

import org.springframework.stereotype.Component;

import com.example.demo.bean.ProductBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.UpdateProductImageRequest;
import com.example.demo.dto.UpdateProductRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.FileStorageService;

@Component
public class UpdateProductImageValidator implements CustValidator<UpdateProductImageRequest,ProductBean> {

    private final ProductRepository productRepository;
    private final ImageRules imageRules;
    private final FileStorageService fileStorageService;

    public UpdateProductImageValidator(ProductRepository productRepository, ImageRules imageRules, FileStorageService fileStorageService) {
        this.productRepository = productRepository;
        this.imageRules = imageRules;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return UpdateProductRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public ProductBean validate(UpdateProductImageRequest target) {
        ErrorInfo errorInfo = new ErrorInfo();
        ProductBean product = productRepository.findById(target.getProductId())
                .orElseThrow(() -> {
                    errorInfo.addError("id", "Product not found");
                    return new ApiException("Product not found", 404, errorInfo);
                });

        imageRules.check(target.getFile());
        String imageUrl = fileStorageService.storeFile(target.getFile());
        product.setImageURL(imageUrl);

        return product;
    }
    
}
