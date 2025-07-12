package com.travel.userservice.dto;

import com.travel.userservice.entity.UserRole;
import lombok.Data;

@Data
public class UserSearchRequest {
    private String name;
    private String email;
    private UserRole role;
    private Boolean active;
} 