import model.Order;
import service.*;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {

        BlockingQueue<Order> queue = new ArrayBlockingQueue<>(10);

        InventoryService inventoryService = new InventoryService();
        PaymentService paymentService = new PaymentService();
        OrderStatusService statusService = new OrderStatusService();
        RateLimiterService rateLimiter = new RateLimiterService(2);

        OrderProducer producer = new OrderProducer(queue);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(new OrderConsumer(queue, inventoryService, paymentService, statusService, rateLimiter));
        executor.submit(new OrderConsumer(queue, inventoryService, paymentService, statusService, rateLimiter));
        executor.submit(new OrderConsumer(queue, inventoryService, paymentService, statusService, rateLimiter));

        producer.placeOrder("Laptop", 2);
        producer.placeOrder("Phone", 5);
        producer.placeOrder("Laptop", 4);
        producer.placeOrder("Laptop", 6);

        Thread.sleep(4000);

        inventoryService.printInventory();
        statusService.printAllStatuses();

        executor.shutdownNow();
    }
}
