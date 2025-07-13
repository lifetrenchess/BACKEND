package com.travel.userservice.service;

import java.util.List;

import com.travel.userservice.model.AuthDto;
import com.travel.userservice.model.UserDto;
import com.travel.userservice.model.UserProfileDto;
import com.travel.userservice.model.UserSearchDto;

public interface UserService {
	public UserDto registerUser(UserDto userDto);
	public UserDto createUserByAdmin(UserDto userDto);
	public UserDto getUserById(Long userId);
	public List<UserDto> getAllUsers();
	public UserDto updateUser(Long userId, UserDto userDto);
	public UserDto updateUserProfile(Long userId, UserProfileDto profileDto);
	
	
	public String authenticateUser(AuthDto authDto);
	
	public List<UserDto> searchUsers(UserSearchDto searchDto);
	public void deleteUser(Long userId);
	public UserDto getUserByEmail(String email);
//	private boolean matchesSearchCriteria(User user, UserSearchDto searchDto);

}
