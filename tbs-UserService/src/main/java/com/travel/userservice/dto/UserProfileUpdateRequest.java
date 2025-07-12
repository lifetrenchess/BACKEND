package com.travel.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    @NotBlank(message = "Name is required")
    private String userName;
    

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String userEmail;

    @NotBlank(message = "Contact number is required")
    private String userContactNumber;
} 