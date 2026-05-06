package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String restaurantName;
    private String items;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String deliveryAddress;
    private LocalDateTime createdAt;
    private String details;

    public enum OrderStatus {
        PENDING, CONFIRMED, FALLBACK
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ✅ getters
    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getDeliveryAddress() { return deliveryAddress; }

    // ✅ setters
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setDetails(String details) { this.details = details; }
}