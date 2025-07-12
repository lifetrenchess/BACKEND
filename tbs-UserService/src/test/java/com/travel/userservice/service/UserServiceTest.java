package com.travel.userservice.service;

import com.travel.userservice.config.JwtUtility;
import com.travel.userservice.entity.User;
import com.travel.userservice.entity.UserRole;
import com.travel.userservice.model.*;
import com.travel.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Enable Mockito extension for JUnit 5
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // Create mock dependencies
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtility jwtUtility;


    // Inject mocks into the class under test
    @InjectMocks
    private UserService userService;

    // Sample data used across tests
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        // Initialize mock User entity
        user = new User();
        user.setUserId(1L);
        user.setUserName("Ayush");
        user.setUserEmail("ayush@example.com");
        user.setUserPassword("Password@123");
        user.setUserContactNumber("9876543210");
        user.setUserRole(UserRole.USER);
        user.setActive(true);

        // Initialize corresponding DTO
        userDto = new UserDto(
                1L,
                "Ayush",
                "ayush@example.com",
                "Password@123",
                UserRole.USER,
                "9876543210",
                true
        );
    }

    @Test
    void createUser_success() {
        // Mock behavior: email doesn't exist
        when(userRepository.existsByUserEmail(userDto.getUserEmail())).thenReturn(false);

        // Simulate mapping DTO → entity
        when(modelMapper.map(userDto, User.class)).thenReturn(user);

        // Simulate save in DB
        when(userRepository.save(user)).thenReturn(user);

        // Simulate mapping saved entity → DTO
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Call method under test
        UserDto result = userService.createUser(userDto);

        // Verify output and interaction
        assertEquals("Ayush", result.getUserName());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_emailExists_shouldThrowException() {
        // Simulate duplicate email check
        when(userRepository.existsByUserEmail(userDto.getUserEmail())).thenReturn(true);

        // Verify that exception is thrown and nothing is saved
        assertThrows(RuntimeException.class, () -> userService.createUser(userDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_success() {
        // Simulate successful fetch
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Call method and assert result
        UserDto result = userService.getUserById(1L);
        assertEquals("Ayush", result.getUserName());
    }

    @Test
    void getUserById_notFound_shouldThrowException() {
        // Simulate user not found
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify exception is thrown
        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllUsers_success() {
        // Mock repository return value
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Call service and assert results
        List<UserDto> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("Ayush", result.get(0).getUserName());
    }

    @Test
    void updateUser_success() {
        // Simulate fetch and mapping
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(modelMapper).map(userDto, user);
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Assert result of update
        UserDto result = userService.updateUser(1L, userDto);
        assertEquals("Ayush", result.getUserName());
    }

    @Test
    void updateUser_notFound_shouldThrowException() {
        // Simulate user not found
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userDto));
    }

    @Test
    void deleteUser_success() {
        // Simulate existence check
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Perform deletion
        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_notFound_shouldThrowException() {
        // Simulate user doesn't exist
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void authenticateUser_success() {
        AuthDto authDto = new AuthDto("ayush@example.com", "Password@123");

        // Simulate successful authentication
        when(userRepository.findByUserEmail(authDto.getUserEmail())).thenReturn(Optional.of(user));
        doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        when(jwtUtility.generateToken(user.getUserEmail())).thenReturn("jwt-token");

        String result = userService.authenticateUser(authDto);
        assertEquals("jwt-token", result);
    }


    @Test
    void authenticateUser_invalidPassword_shouldThrowException() {
        AuthDto authDto = new AuthDto("ayush@example.com", "WrongPassword");

        // Simulate incorrect password
        when(userRepository.findByUserEmail(authDto.getUserEmail())).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> userService.authenticateUser(authDto));
    }

    @Test
    void updateUserProfile_success() {
        UserProfileDto profileDto = new UserProfileDto("Ayush Updated", "ayush@example.com", "9876543210");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(modelMapper).map(profileDto, user);
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.updateUserProfile(1L, profileDto);
        assertEquals("Ayush", result.getUserName());
    }

    @Test
    void searchUsers_matchFound() {
        UserSearchDto searchDto = new UserSearchDto("ayush", "ayush@example.com", UserRole.USER, "9876", true);

        // Return a user that matches the filter
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        List<UserDto> result = userService.searchUsers(searchDto);
        assertEquals(1, result.size());
        assertEquals("Ayush", result.get(0).getUserName());
    }

    @Test
    void searchUsers_noMatch() {
        UserSearchDto searchDto = new UserSearchDto("John", "john@example.com", UserRole.USER, "0000", true);

        // Return a user that doesn't match
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.searchUsers(searchDto);
        assertTrue(result.isEmpty());
    }
}

//package com.travel.userservice.service;
//
//import com.travel.userservice.model.UserDto;
//import com.travel.userservice.entity.User;
//import com.travel.userservice.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//
////import java.util.Arrays;
////import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private UserService userService;
//
//    private User user;
//    private UserDto userDto;
//
//    @BeforeEach
//    void setUp() {
//        user = new User();
//        user.setUserId(1L);
//        user.setUserName("testuser");
//        user.setUserEmail("test@example.com");
//        user.setUserPassword("password");
//        user.setUserContactNumber("9987749400");
////        user.setFirstName("Test");
////        user.setLastName("User");
////        user.setRole(UserRole.CUSTOMER);
//
//        userDto = new UserDto();
//        userDto.setUserId(1L);
//        userDto.setUserName("testuser");
//        userDto.setUserEmail("test@example.com");
//        userDto.setUserPassword("password");
//        userDto.setUserContactNumber("9987749400");
////        userDto.setFirstName("Test");
////        userDto.setLastName("User");
////        userDto.setRole(UserRole.CUSTOMER);
//    }
//
//    @Test
//    void createUser_Success() {
//        when(modelMapper.map(any(UserDto.class), eq(User.class))).thenReturn(user);
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);
//
//        UserDto result = userService.createUser(userDto);
//
//        assertNotNull(result);
//        assertEquals(userDto.getUserName(), result.getUserName());
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void getUserById_Success() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);
//
//        UserDto result = userService.getUserById(1L);
//
//        assertNotNull(result);
//        assertEquals(userDto.getUserId(), result.getUserId());
//        verify(userRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void getUserById_NotFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
//        verify(userRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void updateUser_Success() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);
//
//        UserDto result = userService.updateUser(1L, userDto);
//
//        assertNotNull(result);
//        assertEquals(userDto.getUserName(), result.getUserName());
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void deleteUser_Success() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        doNothing().when(userRepository).delete(any(User.class));
//
//        userService.deleteUser(1L);
//
//        verify(userRepository, times(1)).delete(any(User.class));
//    }
//
////    @Test
////    void searchUsers_Success() {
////        List<User> users = Arrays.asList(user);
////        when(userRepository.findAll()).thenReturn(users);
////        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);
////
////        List<UserDto> results = userService.searchUsers(null, null);
////
////        assertNotNull(results);
////        assertFalse(results.isEmpty());
////        assertEquals(1, results.size());
////        verify(userRepository, times(1)).findAll();
////    }
//} 