package com.example.demo.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class FileUploadFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        
        // Check if this is a multipart request
        String contentType = headers.getFirst(HttpHeaders.CONTENT_TYPE);
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            // For multipart requests, we need to ensure proper handling
            // The gateway will automatically handle the multipart data
            // Just ensure headers are properly set
            ServerHttpRequest modifiedRequest = request.mutate()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .build();
            
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }
        
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100; // High priority to handle file uploads early
    }
} 