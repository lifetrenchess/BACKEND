package com.travel.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.userservice.entity.UserRole;
import com.travel.userservice.model.*;
import com.travel.userservice.service.UserService;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto sampleUserDto() {
        return new UserDto(
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
    void registerUserTest() throws Exception {
        UserDto userDto = sampleUserDto();

        when(userService.registerUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("Ayush"));
    }

    @Test
    void getUserByIdTest() throws Exception {
        UserDto userDto = sampleUserDto();

        when(userService.getUserById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("ayush@example.com"));
    }

    @Test
    void getAllUsersTest() throws Exception {
        List<UserDto> users = List.of(
            sampleUserDto(),
            new UserDto(2L, "Ravi", "ravi@example.com", "Password@123", UserRole.USER, "9988776655", true)
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void updateUserTest() throws Exception {
        UserDto updatedDto = new UserDto(
            1L,
            "Ayush Updated",
            "updated@example.com",
            "Password@123",
            UserRole.USER,
            "9876543210",
            true
        );

        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Ayush Updated"));
    }

    
//    						WITHOUT SPRING SECURITY(OLD)
//    @Test
//    void deleteUserTest() throws Exception {
//        doNothing().when(userService).deleteUser(1L);
//
//        mockMvc.perform(delete("/api/users/1"))
//                .andExpect(status().isNoContent());
//    }
    
//    						WITH SPRING SECURITY
    @Test
    void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User with Id1 has been deleted successfully."));
    }


    @Test
    void updateUserProfileTest() throws Exception {
        UserProfileDto profileDto = new UserProfileDto(
            "Ayush Updated",
            "ayush_updated@example.com",
            "9876543210"
        );

        UserDto updatedUser = new UserDto(
            1L,
            "Ayush Updated",
            "ayush_updated@example.com",
            "Password@123",
            UserRole.USER,
            "9876543210",
            true
        );

        when(userService.updateUserProfile(eq(1L), any(UserProfileDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Ayush Updated"))
                .andExpect(jsonPath("$.userEmail").value("ayush_updated@example.com"))
                .andExpect(jsonPath("$.userContactNumber").value("9876543210"));
    }



//    @Test
//    void authenticateUserTest() throws Exception {
//        AuthDto authDto = new AuthDto("ayush@example.com", "Password@123");
//        UserDto userDto = sampleUserDto();
//
//        when(userService.authenticateUser(any(AuthDto.class))).thenReturn(userDto);
//
//        mockMvc.perform(post("/api/users/auth")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(authDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userEmail").value("ayush@example.com"));
//    }
    
    @Test
    void authenticateUserTest() throws Exception {
        AuthDto authDto = new AuthDto("ayush@example.com", "Password@123");

        when(userService.authenticateUser(any(AuthDto.class))).thenReturn("Login successful");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }


    @Test
    void searchUsersTest() throws Exception {
        UserSearchDto searchDto = new UserSearchDto(
            "Ayush",
            "ayush@example.com",
            UserRole.USER,
            "9876543210",
            true
        );

        List<UserDto> result = List.of(sampleUserDto());

        when(userService.searchUsers(any(UserSearchDto.class))).thenReturn(result);

        mockMvc.perform(post("/api/users/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].userName").value("Ayush"))
                .andExpect(jsonPath("$[0].userEmail").value("ayush@example.com"));
    }

}



//package com.travel.userservice.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
////import com.travel.userservice.entity.User;
//import com.travel.userservice.model.UserDto;
//
//import com.travel.userservice.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
////import java.util.Arrays;
////import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    void createUser_Success() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setUserId(1L);
//        userDto.setUserName("testuser");
//        userDto.setUserEmail("test@example.com");
//        userDto.setUserPassword("password");
//        userDto.setUserContactNumber("9987749400");
////      userDto.setFirstName("Test");
////      userDto.setLastName("User");
//
//        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);
//
//        mockMvc.perform(post("/api/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(userDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username").value(userDto.getUserName()))
//                .andExpect(jsonPath("$.email").value(userDto.getUserEmail()));
//    }
//
//    @Test
//    void getUserById_Success() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setUserId(1L);
//        userDto.setUserName("testuser");
//        userDto.setUserEmail("test@example.com");
//
//        when(userService.getUserById(1L)).thenReturn(userDto);
//
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.username").value(userDto.getUserName()));
//    }
//
//    @Test
//    void updateUser_Success() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setUserId(1L);
//        userDto.setUserName("updateduser");
//        userDto.setUserEmail("updated@example.com");
//
//        when(userService.updateUser(any(Long.class), any(UserDto.class))).thenReturn(userDto);
//
//        mockMvc.perform(put("/api/users/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(userDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value(userDto.getUserName()));
//    }
//
//    @Test
//    void deleteUser_Success() throws Exception {
//        mockMvc.perform(delete("/api/users/1"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void searchUsers_Success() throws Exception {
//        List<UserDto> users = Arrays.asList(
//            new UserDto(1L, "user1", "user1@example.com", "First1", "Last1", UserRole.CUSTOMER),
//            new UserDto(2L, "user2", "user2@example.com", "First2", "Last2", UserRole.CUSTOMER)
//        );
//
//        when(userService.searchUsers(null, null)).thenReturn(users);
//
//        mockMvc.perform(get("/api/users/search"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].username").value("user1"))
//                .andExpect(jsonPath("$[1].username").value("user2"));
//    }
//} 