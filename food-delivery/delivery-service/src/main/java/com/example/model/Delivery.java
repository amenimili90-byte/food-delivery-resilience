package com.fooddelivery.model;

import java.time.LocalDateTime;

public class Delivery {

    private Long id;
    private Long orderId;
    private String customerName;
    private String address;
    private String driverName;
    private String status;
    private LocalDateTime assignedAt;

    public Delivery() {}

    public Delivery(Long id, Long orderId, String customerName,
                    String address, String driverName,
                    String status, LocalDateTime assignedAt) {
        this.id = id;
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.driverName = driverName;
        this.status = status;
        this.assignedAt = assignedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}