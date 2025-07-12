package com.travel.userservice.config;
 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.http.HttpMethod;
 
 
 
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig {
   
    @Autowired
    JwtFilter jwtFilter;
   
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
         httpSecurity
            .cors(Customizer.withDefaults()) // Enable CORS
            .authorizeHttpRequests(auth ->
                auth.requestMatchers(HttpMethod.POST, "/user-api/users").permitAll()
                    .requestMatchers(HttpMethod.GET, "/user-api/users/{userId}").permitAll()
                    .requestMatchers("/user-api/users/login").permitAll()
                    .anyRequest().authenticated()
            );
         httpSecurity.csrf(csrf-> csrf.disable());
         httpSecurity.addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);
         httpSecurity.httpBasic(Customizer.withDefaults());
         return httpSecurity.build();
       
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
   
    @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
}
 
 