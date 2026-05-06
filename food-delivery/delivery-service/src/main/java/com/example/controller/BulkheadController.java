package com.fooddelivery.controller;

import com.fooddelivery.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BulkheadController {

    private final TaskService taskService;

    public BulkheadController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/semaphore/test")
    public String testSemaphore() {
        return taskService.processTask(1, 1000);
    }

    @GetMapping("/threadpool/test")
    public CompletableFuture<String> testThread() {
        return taskService.processAsyncTask(1, 2000);
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of("tasks", taskService.getCounter());
    }
}