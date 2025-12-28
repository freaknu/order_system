package service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class InventoryService {
    private final ConcurrentHashMap<String,Integer>inventory = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public synchronized void addProduct(String product,int stock) {
        inventory.put(product,inventory.getOrDefault(product,0)+stock);
    }

    public boolean reduceStock(String product, int quantity) {
        lock.lock();
        try {
            int available = inventory.getOrDefault(product, 0);
            if (available >= quantity) {
                inventory.put(product, available - quantity);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
    public void printInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}
