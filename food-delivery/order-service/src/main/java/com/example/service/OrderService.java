package com.example.service;

import com.example.model.Order;
import com.example.model.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private static final String DELIVERY_SERVICE = "deliveryService";

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository,
                        RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    // =========================
    // CREATE ORDER (REAL CB)
    // =========================
    @CircuitBreaker(name = DELIVERY_SERVICE, fallbackMethod = "createOrderFallback")
    public Order createOrder(Order order) {

        log.info("Creating order for: {}", order.getCustomerName());

        String url = "http://localhost:8081/api/deliveries/assign";

        Map<String, Object> req = Map.of(
                "orderId", order.getId() == null ? 0 : order.getId(),
                "customerName", order.getCustomerName(),
                "address", order.getDeliveryAddress()
        );

        restTemplate.postForObject(url, req, Map.class);

        order.setStatus(Order.OrderStatus.CONFIRMED);
        order.setDetails("Order confirmed");

        return orderRepository.save(order);
    }

    // fallback
    public Order createOrderFallback(Order order, Throwable t) {
        log.error("FALLBACK createOrder: {}", t.getMessage());

        order.setStatus(Order.OrderStatus.FALLBACK);
        order.setDetails("Delivery service unavailable");

        return orderRepository.save(order);
    }

    // =========================
    // CIRCUIT BREAKER TEST
    // =========================
    @CircuitBreaker(name = DELIVERY_SERVICE, fallbackMethod = "testFallback")
    public String testFailure() {
        throw new RuntimeException("Simulated failure for circuit breaker");
    }

    public String testFallback(Throwable t) {
        return "Fallback: Delivery Service unavailable (" + t.getMessage() + ")";
    }

    // =========================
    // SUCCESS TEST
    // =========================
    @CircuitBreaker(name = DELIVERY_SERVICE, fallbackMethod = "testFallback")
    public String testSuccess() {
        return "SUCCESS: Delivery working";
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public String ping() {
        return "order-service OK";
    }
}