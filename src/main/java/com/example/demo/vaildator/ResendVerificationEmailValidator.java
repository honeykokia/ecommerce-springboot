package com.example.demo.vaildator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.bean.UserBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.ResendVerificationEmailRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UserRepository;

@Component
public class ResendVerificationEmailValidator implements CustValidator<ResendVerificationEmailRequest, UserBean> {

    private final UserRepository userRepository;
    private ResendVerificationEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return ResendVerificationEmailRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public UserBean validate(ResendVerificationEmailRequest request) {
        Optional<UserBean> userOptional = userRepository.findByEmail(request.getEmail());
        UserBean user = userOptional.orElseThrow(() -> {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("email", "email not found");
            throw new ApiException("email not found", 400, errorInfo);
        });

        return user;
    }



}
