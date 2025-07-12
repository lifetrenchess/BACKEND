package com.travel.userservice.model;

import com.travel.userservice.entity.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
	
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name can only contain letters and spaces")
    private String userName;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String userEmail;

    private UserRole userRole;
    
    private String userContactNumber;

    private Boolean active;
} 