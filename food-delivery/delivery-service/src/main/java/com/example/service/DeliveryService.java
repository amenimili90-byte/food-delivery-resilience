package com.fooddelivery.service;

import com.fooddelivery.model.Delivery;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    private final List<Delivery> deliveries = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    private final String[] drivers = {
            "Ali Ben Salah", "Sami Trabelsi", "Mohamed Chaabani",
            "Yassine Mejri", "Riadh Hamdi"
    };

    @Bulkhead(name = "deliveryPool", fallbackMethod = "fallbackAssign")
    public Delivery assignDelivery(Map<String, Object> request) throws InterruptedException {

        log.info("Assigning delivery for {}", request.get("customerName"));

        Thread.sleep(70);

        String driver = drivers[new Random().nextInt(drivers.length)];

        Delivery delivery = new Delivery(
                counter.getAndIncrement(),
                Long.valueOf(request.get("orderId").toString()),
                request.get("customerName").toString(),
                request.get("address").toString(),
                driver,
                "ASSIGNED",
                LocalDateTime.now()
        );

        deliveries.add(delivery);

        log.info("Delivery assigned successfully");

        return delivery;
    }

    // fallback BulkheadFullException
    public Delivery fallbackAssign(Map<String, Object> request, BulkheadFullException e) {

        return new Delivery(
                -1L,
                Long.valueOf(request.getOrDefault("orderId", "0").toString()),
                request.getOrDefault("customerName", "unknown").toString(),
                request.getOrDefault("address", "").toString(),
                "N/A",
                "REJECTED - FULL",
                LocalDateTime.now()
        );
    }

    // fallback général
    public Delivery fallbackAssign(Map<String, Object> request, Throwable t) {

        return new Delivery(
                -1L,
                0L,
                request.getOrDefault("customerName", "unknown").toString(),
                "",
                "N/A",
                "ERROR",
                LocalDateTime.now()
        );
    }

    public List<Delivery> getAllDeliveries() {
        return deliveries;
    }

    public String ping() {
        return "OK";
    }
}