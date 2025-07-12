package com.travel.userservice.model;

import com.travel.userservice.entity.UserRole;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name can only contain letters and spaces")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String userEmail;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[1-9]\\d{9,14}$", message = "Invalid contact number format")
    private String userContactNumber;
} 