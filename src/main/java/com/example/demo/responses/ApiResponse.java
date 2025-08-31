package com.example.demo.responses;

import java.time.LocalDateTime;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {

    private LocalDateTime timestamp;
    private Object data;

    public ApiResponse(Object data) {
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }
    
}
