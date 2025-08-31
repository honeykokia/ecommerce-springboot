package com.example.demo.vaildator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.bean.PromotionBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.UpdatePromotionImageRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.PromotionRepository;

@Component
public class UpdatePromotionImageValidator implements CustValidator<UpdatePromotionImageRequest, PromotionBean> {

    private final PromotionRepository promotionRepository;

    public UpdatePromotionImageValidator(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return UpdatePromotionImageRequest.class.equals(clazz);
    }

    @Override
    public PromotionBean validate(UpdatePromotionImageRequest target) {
        ErrorInfo errorInfo = new ErrorInfo();
        PromotionBean promotion = promotionRepository.findById(target.getPromotionId())
        .orElseThrow(() -> {
            errorInfo.addError("promotionId", "Promotion not found");
            return new ApiException("Promotion not found", 404, errorInfo);
        });
        
        if(target.getFile() == null || target.getFile().isEmpty()) {
            errorInfo.addError("file", "File is required");
            throw new ApiException("Validation failed", 400, errorInfo);
        }

        String originalFilename = target.getFile().getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            errorInfo.addError("file", "File name cannot be empty");
        }else{
            if (originalFilename.length() > 255) {
                errorInfo.addError("file", "File name is too long");
            }

            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext.toLowerCase())) {
                errorInfo.addError("file", "File type must be jpg, jpeg, png, or webp");
            }
        }

        
        if (errorInfo.hasErrors()) {
            throw new ApiException("Validation failed", 400, errorInfo);
        }

        String uploadDir = System.getProperty("user.dir") + "/upload/";
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        String filename = UUID.randomUUID() + "_" + target.getFile().getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);
        try {
            target.getFile().transferTo(path.toFile());
        } catch (IOException e) {
            errorInfo.addError("file", "File upload failed");
            throw new ApiException("File upload failed", 500, errorInfo);
        }

        promotion.setImageURL("/upload/" + filename);
        return promotion;
    }

}