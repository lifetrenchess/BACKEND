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
        System.out.println("user email is"+username);
       
        Optional<User> optionaluser = userRepository.findByUserEmail(username);
        System.out.println("User details are: "+optionaluser.get());
        if(optionaluser.isEmpty()) {
            throw new UsernameNotFoundException ("Email is no there in the database");
        }
       
        User user = optionaluser.get();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name());
        org.springframework.security.core.userdetails.User u = new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPassword(), true, true, true, true, Collections.singletonList(authority));
        return u;
    }
 
}
 
 