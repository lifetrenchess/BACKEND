package com.travel.userservice.service;

import com.travel.userservice.entity.User;
import com.travel.userservice.entity.UserRole;
import com.travel.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUserId(1L);
        user.setUserEmail("ayush@example.com");
        user.setUserPassword("encodedPassword");
        user.setUserRole(UserRole.ADMIN);
        user.setActive(true);
    }

    @Test
    void loadUserByUsername_success() {
        when(userRepository.findByUserEmail("ayush@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("ayush@example.com");

        assertNotNull(userDetails);
        assertEquals("ayush@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_userNotFound() {
        when(userRepository.findByUserEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
            customUserDetailsService.loadUserByUsername("missing@example.com"));
    }
}
