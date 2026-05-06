package com.example.controller;

import com.example.service.OrderService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CircuitBreakerController {

    private final OrderService orderService;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerController(OrderService orderService,
                                    CircuitBreakerRegistry circuitBreakerRegistry) {
        this.orderService = orderService;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    // ==============================
    // 1. TEST FAIL (trigger circuit)
    // ==============================
    @GetMapping("/test-failure")
    public Map<String, Object> testFailure() {
        String result = orderService.testFailure();
        return createResponse(result);
    }

    // ==============================
    // 2. TEST SUCCESS
    // ==============================
    @GetMapping("/test-success")
    public Map<String, Object> testSuccess() {
        String result = orderService.testSuccess();
        return createResponse(result);
    }

    // ==============================
    // 3. STATUS CIRCUIT BREAKER
    // ==============================
    @GetMapping("/status")
    public Map<String, Object> getStatus() {

        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker("deliveryService");

        Map<String, Object> status = new HashMap<>();

        status.put("state", circuitBreaker.getState().toString());

        status.put("metrics", Map.of(
                "failureRate", circuitBreaker.getMetrics().getFailureRate() + "%",
                "successfulCalls", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls(),
                "failedCalls", circuitBreaker.getMetrics().getNumberOfFailedCalls(),
                "notPermittedCalls", circuitBreaker.getMetrics().getNumberOfNotPermittedCalls()
        ));

        return status;
    }

    // ==============================
    // 4. RESET CIRCUIT BREAKER
    // ==============================
    @PostMapping("/reset")
    public Map<String, String> reset() {

        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker("deliveryService");

        circuitBreaker.reset();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Circuit reset successfully");
        response.put("state", circuitBreaker.getState().toString());

        return response;
    }

    // ==============================
    // Helper
    // ==============================
    private Map<String, Object> createResponse(String result) {

        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker("deliveryService");

        Map<String, Object> response = new HashMap<>();

        response.put("result", result);
        response.put("circuitState", circuitBreaker.getState().toString());

        return response;
    }
}