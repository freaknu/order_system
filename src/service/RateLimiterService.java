package service;

import java.util.concurrent.Semaphore;

public class RateLimiterService {

    private final Semaphore semaphore;

    public RateLimiterService(int maxConcurrentOrders) {
        this.semaphore = new Semaphore(maxConcurrentOrders);
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }
}

