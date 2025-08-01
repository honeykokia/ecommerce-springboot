package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.bean.UserBean;
import com.example.demo.dto.LoginInfo;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.enums.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.responses.ApiResponse;
import com.example.demo.utils.JwtUtil;
import com.example.demo.vaildator.LoginValidator;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginValidator loginValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ApiResponse login(LoginRequest loginRequest) {

        UserBean user = loginValidator.validate(loginRequest);       
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName(user.getName());
        loginInfo.setEmail(user.getEmail());
        loginInfo.setToken(token);

        return new ApiResponse(Map.of("user",loginInfo));
    }

    public ApiResponse register(RegisterRequest registerRequest) {

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        UserBean user = new UserBean();
        user.setName("新加入會員");
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setImage("/upload/defaultAvatar.png");
        user.setGender((byte) 1);
        user.setBirthday(null);
        user.setPhone(null);
        user.setCity(null);
        user.setCountry(null);
        user.setAddress(null);
        user.setRole(UserRole.MEMBER);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLoginAt(LocalDateTime.now());

        userRepository.save(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getName());
        userInfo.setEmail(user.getEmail());

        return new ApiResponse(Map.of("user", userInfo));

    }
}
