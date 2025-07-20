package com.travel.userservice.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.userservice.exception.UserAlreadyExistsException;
import com.travel.userservice.config.JwtUtility;
//import com.travel.userservice.exception.InvalidCredentialsException;
import com.travel.userservice.entity.User;
import com.travel.userservice.entity.UserRole;
import com.travel.userservice.model.AuthDto;
import com.travel.userservice.model.UserDto;
import com.travel.userservice.model.UserProfileDto;
import com.travel.userservice.model.UserSearchDto;
import com.travel.userservice.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtility jwtUtility;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	@Transactional
	public UserDto registerUser(UserDto userDto) {
		if (userRepository.existsByUserEmail(userDto.getUserEmail())) {
			throw new UserAlreadyExistsException("User with IDs already exists");
		}

		User user = modelMapper.map(userDto, User.class);

		// 🔒 Always set USER role for new registrations (Admin/Agent are pre-registered)
		user.setUserRole(UserRole.USER);

		// 🔒 Encode the password before saving
	    user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

		User savedUser = userRepository.save(user);
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Transactional
	public UserDto createUserByAdmin(UserDto userDto) {
		if (userRepository.existsByUserEmail(userDto.getUserEmail())) {
			throw new UserAlreadyExistsException("User with this email already exists");
		}

		User user = modelMapper.map(userDto, User.class);
		// Allow any role assignment (ADMIN, AGENT, USER)
		user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

		User savedUser = userRepository.save(user);
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public String authenticateUser(AuthDto authDto) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authDto.getUserEmail(), authDto.getUserPassword()));
		} catch (BadCredentialsException ex) {
			throw new com.travel.userservice.exception.InvalidCredentialsException(
					"Incorrect password for email: " + authDto.getUserEmail());
		}

		User user = userRepository.findByUserEmail(authDto.getUserEmail())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		String token = jwtUtility.generateToken(user.getUserEmail());

		return new String(token);
	}

	@Override
	public UserDto getUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
	}

	@Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return modelMapper.map(user, UserDto.class);
    }

	@Override
	@Transactional
	public UserDto updateUser(Long userId, UserDto userDto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// Skip mapping the ID field to avoid setting it to null
		modelMapper.typeMap(UserDto.class, User.class).addMappings(mapper -> mapper.skip(User::setUserId));

		modelMapper.map(userDto, user);

		User updatedUser = userRepository.save(user);
		return modelMapper.map(updatedUser, UserDto.class);
	}

	@Transactional
	@Override
	public UserDto updateUserProfile(Long userId, UserProfileDto profileDto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		modelMapper.map(profileDto, user);
		User updatedUser = userRepository.save(user);
		return modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public List<UserDto> searchUsers(UserSearchDto searchDto) {
		return userRepository.findAll().stream()
				.filter(user -> matchesSearchCriteria(user, searchDto))
				.map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
	}

	private boolean matchesSearchCriteria(User user, UserSearchDto searchDto) {
		if (searchDto.getUserName() != null && !user.getUserName().toLowerCase().contains(searchDto.getUserName().toLowerCase())) {
			return false;
		}
		if (searchDto.getUserEmail() != null && !user.getUserEmail().toLowerCase().contains(searchDto.getUserEmail().toLowerCase())) {
			return false;
		}
		if (searchDto.getUserRole() != null && user.getUserRole() != searchDto.getUserRole()) {
			return false;
		}
		if (searchDto.getActive() != null && user.isActive() != searchDto.getActive()) {
			return false;
		}
		if (searchDto.getUserContactNumber() != null && !user.getUserContactNumber().contains(searchDto.getUserContactNumber())) {
			return false;
		}

		return true;
	}
	
	
	@Override
	@Transactional
	public void deleteUser(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new RuntimeException("User not found");
		}
		userRepository.deleteById(userId);
	}

//    private boolean matchesSearchCriteria(User user, UserSearchDto searchDto) {
//        if (searchDto.getUserName() != null && !user.getUserName().toLowerCase().contains(searchDto.getUserName().toLowerCase())) {
//            return false;
//        }
//        if (searchDto.getUserEmail() != null && !user.getUserEmail().toLowerCase().contains(searchDto.getUserEmail().toLowerCase())) {
//            return false;
//        }
//        if (searchDto.getUserRole() != null && user.getUserRole() != searchDto.getUserRole()) {
//            return false;
//        }
//        if (searchDto.getActive() != null && user.isActive() != searchDto.getActive()) {
//            return false;
//        }
//        if (searchDto.getUserContactNumber() != null && !user.getUserContactNumber().contains(searchDto.getUserContactNumber())) {
//            return false;
//        }
//
//        return true;
//    }
//	String encodedPassword = passwordEncoder.encode(userDto.getUserPassword());
//	System.out.println("Encoded password: " + encodedPassword);
//	user.setUserPassword(encodedPassword);
}
