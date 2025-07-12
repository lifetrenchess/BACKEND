package com.travel.userservice.dto;

import com.travel.userservice.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "Name is required")
    private String userName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String userEmail;

    @NotBlank(message = "Password is required")
    private String userPassword;

    private UserRole userRole;

    @NotBlank(message = "Contact number is required")
    private String userContactNumber;
} 