package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @JsonIgnore
    @Schema(hidden = true)
    private Long userId;

    @NotBlank(message = "old password is required")
    private String oldPassword;

    @NotBlank(message = "new password is required")
    private String newPassword;

    @NotBlank(message = "confirm new password is required")
    private String newConfirmPassword;
}
