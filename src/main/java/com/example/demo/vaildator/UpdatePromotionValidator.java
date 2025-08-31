package com.example.demo.vaildator;

import org.springframework.stereotype.Component;

import com.example.demo.bean.PromotionBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.UpdatePromotionRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.PromotionRepository;

@Component
public class UpdatePromotionValidator implements CustValidator<UpdatePromotionRequest, PromotionBean>{

    private final PromotionRepository promotionRepository;

    public UpdatePromotionValidator(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return UpdatePromotionRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public PromotionBean validate(UpdatePromotionRequest target) {
        ErrorInfo errorInfo = new ErrorInfo();
        PromotionBean promotion = promotionRepository.findById(target.getId())
        .orElseThrow(() -> {
            errorInfo.addError("id", "Promotion not found");
            throw new ApiException("Promotion not found", 404, errorInfo);
        });

        if (target.getName() != null && target.getName().length() > 255) {
            errorInfo.addError("name", "Name must be less than 255 characters");
        }

        if (target.getStartDate() != null && target.getEndDate() != null) {
            if (target.getStartDate().isAfter(target.getEndDate())) {
                errorInfo.addError("startDate", "Start date cannot be after end date");
            }
        }

        // Perform validation and set properties on the promotion object
        if (errorInfo.hasErrors()) {
            throw new ApiException("Validation failed", 400, errorInfo);
        }

        return promotion;
    }
    

}
