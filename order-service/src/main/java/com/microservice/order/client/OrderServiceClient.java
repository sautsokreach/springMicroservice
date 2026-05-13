package com.microservice.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    @Value("${service.user-url}")
    private String userServiceUrl;

    @Value("${service.product-url}")
    private String productServiceUrl;

    public OrderServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "userServiceFallback")
    @Retry(name = "user-service")
    public Object getUser(Long userId) {
        return restTemplate.getForObject(userServiceUrl + "/users/" + userId, Object.class);
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "productServiceFallback")
    @Retry(name = "product-service")
    public Object getProduct(Long productId) {
        return restTemplate.getForObject(productServiceUrl + "/products/" + productId, Object.class);
    }

    // Fallback: circuit is OPEN or all retries exhausted
    public Object userServiceFallback(Long userId, Exception ex) {
        throw new RuntimeException("User service is unavailable (circuit open). Cause: " + ex.getMessage());
    }

    public Object productServiceFallback(Long productId, Exception ex) {
        throw new RuntimeException("Product service is unavailable (circuit open). Cause: " + ex.getMessage());
    }
}
