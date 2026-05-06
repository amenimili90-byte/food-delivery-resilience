package com.fooddelivery.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {

    private int counter = 0;

    @Bulkhead(name = "semaphoreBulkhead", type = Bulkhead.Type.SEMAPHORE)
    public String processTask(int id, int time) {
        counter++;

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "Task " + id + " processed (count=" + counter + ")";
    }

    @Bulkhead(name = "threadPoolBulkhead", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<String> processAsyncTask(int id, int time) {

        counter++;

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(time);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "Async Task " + id + " done (count=" + counter + ")";
        });
    }

    public int getCounter() {
        return counter;
    }
}