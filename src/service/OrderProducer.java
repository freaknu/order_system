package service;

import model.Order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderProducer {
    private final BlockingQueue<Order> queue;
    private final AtomicInteger orderIdGenerator = new AtomicInteger(1);

    public OrderProducer(BlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public void placeOrder(String product, int quantity) {
        try {
            int orderId = orderIdGenerator.getAndIncrement();
            Order order = new Order(orderId, product, quantity);
            queue.put(order);
            System.out.println("Order placed: " + orderId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
