package service;

import model.Order;
import model.OrderStatus;
import java.util.concurrent.BlockingQueue;

public class OrderConsumer implements Runnable {

    private final BlockingQueue<Order> queue;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final OrderStatusService statusService;
    private final RateLimiterService rateLimiter;

    public OrderConsumer(
            BlockingQueue<Order> queue,
            InventoryService inventoryService,
            PaymentService paymentService,
            OrderStatusService statusService,
            RateLimiterService rateLimiter) {

        this.queue = queue;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.statusService = statusService;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = queue.take();

                rateLimiter.acquire(); // ⛔ rate limit starts

                statusService.updateStatus(order.getOrderId(), OrderStatus.PROCESSING);

                boolean stockOk = inventoryService
                        .reduceStock(order.getProduct(), order.getQuantity());

                if (!stockOk) {
                    statusService.updateStatus(order.getOrderId(), OrderStatus.FAILED);
                    rateLimiter.release();
                    continue;
                }

                paymentService.processPayment(order.getOrderId());

                statusService.updateStatus(order.getOrderId(), OrderStatus.COMPLETED);

                rateLimiter.release(); // ✅ permit returned
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
