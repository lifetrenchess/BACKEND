package cts.tpm.client;

import cts.tpm.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
    
    @GetMapping("/api/users/me")
    UserDto getCurrentUser(@RequestHeader("Authorization") String token);
    
    @GetMapping("/api/users/verify-agent/{agentId}")
    boolean verifyAgentRole(@PathVariable("agentId") Long agentId, @RequestHeader("Authorization") String token);
} 