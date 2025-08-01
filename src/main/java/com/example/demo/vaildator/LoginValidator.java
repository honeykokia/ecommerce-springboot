package com.example.demo.vaildator;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.bean.UserBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.LoginRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UserRepository;

@Component
public class LoginValidator implements CustValidator<LoginRequest,UserBean> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginValidator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return LoginRequest.class.equals(clazz);
    }

    @Override
    public UserBean validate(LoginRequest target) {
        ErrorInfo errorInfo = new ErrorInfo();

        Optional<UserBean> userOpt = userRepository.findByEmail(target.getEmail());

        if (userOpt.isEmpty()) {
            errorInfo.addError("email", "email or password is incorrect");
            throw new ApiException("Validation failed", 400, errorInfo);
        }

        UserBean user = userOpt.get();

        if (!passwordEncoder.matches(target.getPassword(), user.getPassword())) {
            errorInfo.addError("password", "email or password is incorrect");
            throw new ApiException("Validation failed", 400,errorInfo);
        }

        return user;

    }

}
