package com.example.demo.vaildator;

import org.springframework.stereotype.Component;

import com.example.demo.bean.UserBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.ForgetPasswordRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UserRepository;

@Component
public class ForgetPasswordValidator implements CustValidator<ForgetPasswordRequest,UserBean>{

    private final UserRepository userRepository;
    public ForgetPasswordValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return ForgetPasswordRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public UserBean validate(ForgetPasswordRequest request) {
        // Perform validation logic here
        ErrorInfo errorInfo = new ErrorInfo();
        UserBean user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            errorInfo.addError("email", "Email doesn't exists.");
            throw new ApiException("Email not found", 400,errorInfo);
        });
        return user;
    }
    
}
