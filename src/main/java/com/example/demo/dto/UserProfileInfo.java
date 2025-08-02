package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserProfileInfo {
    private String email;
    private String name;
    private String image;
    private Byte gender;
    private LocalDate birthday;
    private String phone;
    private String city;
    private String country;
    private String address;
}
