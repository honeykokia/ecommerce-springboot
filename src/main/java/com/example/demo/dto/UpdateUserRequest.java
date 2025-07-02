package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private byte gender;
    private LocalDate birthday;
    private String phone;
    private String image;
    private String city;
    private String country;
    private String address;
}
