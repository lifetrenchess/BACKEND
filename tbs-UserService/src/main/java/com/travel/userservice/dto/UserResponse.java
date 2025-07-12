package com.travel.userservice.dto;

import com.travel.userservice.entity.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private UserRole role;
    private String contactNumber;
    private boolean active;
} 