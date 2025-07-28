package com.example.demo.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.UserBean;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserInfo;
import com.example.demo.enums.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.responses.ApiResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ApiResponse register(RegisterRequest registerRequest) {

        UserBean user = new UserBean();
        user.setName("新加入會員");
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
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

        return new ApiResponse(userInfo);

    }
}
