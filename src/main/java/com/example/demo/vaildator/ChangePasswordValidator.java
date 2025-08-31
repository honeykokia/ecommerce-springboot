package com.example.demo.vaildator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.bean.UserBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UserRepository;

@Component
public class ChangePasswordValidator implements CustValidator<ChangePasswordRequest, UserBean> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String PASSWORD_PATTERN="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,12}$";

    public ChangePasswordValidator(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return ChangePasswordRequest.class.equals(clazz);
    }

    @Override
    public UserBean validate(ChangePasswordRequest target) {
        ErrorInfo errorInfo = new ErrorInfo();
        UserBean user = userRepository.findById(target.getUserId())
                .orElseThrow(() -> {
                    errorInfo.addError("userId", "connection abnormal");
                    return new ApiException("User not found", 404, errorInfo);
                });
        if(!passwordEncoder.matches(target.getOldPassword(), user.getPassword())) {
            errorInfo.addError("oldPassword", "password is incorrect");
            throw new ApiException("Validation failed", 400, errorInfo);
        }

        if (!target.getNewPassword().equals(target.getNewConfirmPassword())) {
            errorInfo.addError("confirmNewPassword", "new password and confirm new password do not match");
            throw new ApiException("Validation failed", 400, errorInfo);
        }

        if (!target.getNewPassword().matches(PASSWORD_PATTERN)) {
            errorInfo.addError("newPassword", "new password must be 8-12 characters long, contain at least one uppercase letter, one lowercase letter, and one digit");
            throw new ApiException("Validation failed", 400, errorInfo);
        }
 
        return user;
    }


}
