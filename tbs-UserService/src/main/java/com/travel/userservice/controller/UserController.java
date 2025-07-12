
package com.travel.userservice.controller;
 
import com.travel.userservice.model.*;
import com.travel.userservice.service.UserService;
 
import jakarta.validation.Valid;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * REST controller for user management and authentication.
 * Handles registration, login, profile, and admin operations.
 */
@RestController
@RequestMapping("/user-api/users")
public class UserController {
   
    @Autowired
    private UserService userService;
   
 
    // ===================== PUBLIC ENDPOINTS =====================

    /**
     * Register a new user.
     * @param userDto User registration data
     * @return Created user
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
    	System.out.println(userDto);
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    /**
     * Authenticate user and return JWT token.
     * @param authDto Login credentials
     * @return JWT token as string
     */
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody AuthDto authDto) {
        return ResponseEntity.ok(userService.authenticateUser(authDto));
    }

    /**
     * Get user by ID.
     * @param userId User ID
     * @return User details
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    /**
     * Update user profile (name, email, contact).
     * @param userId User ID
     * @param profileDto Profile update data
     * @return Updated user
     */
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserDto> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileDto profileDto) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, profileDto));
    }

    /**
     * Get current user profile from JWT (no userId needed).
     * @param authentication Injected by Spring Security
     * @return Current user details
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        // Assumes username is email
        String email = authentication.getName();
        // You may need to implement a getUserByEmail in your service
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // ===================== PROTECTED ENDPOINTS =====================

    /**
     * Get all users (admin only).
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Update user (admin or travel agent).
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAVEL_AGENT')")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userId, userDto));
    }

    /**
     * Delete user (admin only).
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User with Id " + userId + " has been deleted successfully.");
    }

    /**
     * Search users (admin or travel agent).
     */
    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAVEL_AGENT')")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestBody UserSearchDto searchDto) {
        return ResponseEntity.ok(userService.searchUsers(searchDto));
    }

    // ===================== FUTURE/OPTIONAL ENDPOINTS =====================
    // Add endpoints like /check-email, /change-password as needed for frontend
}
 