package com.travel.userservice.service;
 
import java.util.Collections;
import java.util.Optional;
 
//import javax.security.auth.login.CredentialException;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
 
import com.travel.userservice.entity.User;
import com.travel.userservice.repository.UserRepository;
 
 
@Service
public class CustomUserDetailsService implements UserDetailsService {
   
    @Autowired
    UserRepository userRepository;
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionaluser = userRepository.findByUserEmail(username);
		if (optionaluser.isEmpty()) {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}
		User user = optionaluser.get();
		return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPassword(),
				getAuthorities(user.getUserRole()));
	}
 
}
 
 