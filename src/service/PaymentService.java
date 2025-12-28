package service;

public class PaymentService {

    public boolean processPayment(int orderId) {
        try {
            Thread.sleep(500);
            System.out.println("Payment successful for order " + orderId);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
