package service;

import model.OrderStatus;
import java.util.concurrent.ConcurrentHashMap;

public class OrderStatusService {

    private final ConcurrentHashMap<Integer, OrderStatus> orderStatusMap =
            new ConcurrentHashMap<>();

    public void updateStatus(int orderId, OrderStatus status) {
        orderStatusMap.put(orderId, status);
    }

    public OrderStatus getStatus(int orderId) {
        return orderStatusMap.get(orderId);
    }

    public void printAllStatuses() {
        System.out.println("Order Statuses: " + orderStatusMap);
    }
}
