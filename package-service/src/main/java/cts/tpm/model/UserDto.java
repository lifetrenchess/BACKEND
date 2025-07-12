package cts.tpm.model;

import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userRole;
    private String userContactNumber;
    private boolean active;
} 