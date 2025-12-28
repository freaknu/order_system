# Order System

A concurrent order processing system built with Java that demonstrates producer-consumer pattern, thread synchronization, and rate limiting.

## Overview

This project implements a multi-threaded order management system that handles order placement, inventory management, payment processing, and order status tracking. It showcases various concurrency concepts including blocking queues, semaphores, concurrent data structures, and thread pools.

## Features

- **Producer-Consumer Pattern**: Orders are placed by a producer and consumed by multiple consumer threads
- **Concurrent Order Processing**: Multiple orders can be processed simultaneously by different consumer threads
- **Thread-Safe Inventory Management**: Uses `ReentrantLock` and `ConcurrentHashMap` to ensure thread-safe stock management
- **Rate Limiting**: Limits the number of concurrent order processing using semaphores
- **Order Status Tracking**: Tracks order lifecycle from CREATED → PROCESSING → COMPLETED/FAILED
- **Payment Processing Simulation**: Simulates asynchronous payment processing

## Project Structure

```
OrderSystem/
├── src/
│   ├── Main.java                           # Application entry point
│   ├── model/
│   │   ├── Order.java                      # Order model class
│   │   └── OrderStatus.java                # Order status enum
│   └── service/
│       ├── InventoryService.java           # Thread-safe inventory management
│       ├── OrderConsumer.java              # Order consumer thread
│       ├── OrderProducer.java              # Order producer
│       ├── OrderStatusService.java         # Order status tracker
│       ├── PaymentService.java             # Payment processing simulation
│       └── RateLimiterService.java         # Semaphore-based rate limiter
└── OrderSystem.iml
```

## Architecture

### Components

1. **Order Model**
   - `Order`: Represents an order with orderId, product name, and quantity
   - `OrderStatus`: Enum defining order states (CREATED, PROCESSING, COMPLETED, FAILED)

2. **Services**
   - `OrderProducer`: Creates orders and adds them to a blocking queue
   - `OrderConsumer`: Processes orders from the queue using multiple threads
   - `InventoryService`: Manages product inventory with thread-safe operations
   - `PaymentService`: Simulates payment processing with a delay
   - `OrderStatusService`: Tracks order status using a concurrent hash map
   - `RateLimiterService`: Controls concurrent order processing rate

### Concurrency Mechanisms

- **BlockingQueue**: Thread-safe queue for producer-consumer communication
- **ExecutorService**: Thread pool management with 3 worker threads
- **Semaphore**: Rate limiting to control concurrent order processing
- **ReentrantLock**: Ensures atomic inventory updates
- **ConcurrentHashMap**: Thread-safe storage for inventory and order statuses
- **AtomicInteger**: Thread-safe order ID generation

## How It Works

1. **Order Placement**: `OrderProducer` creates orders and adds them to a `BlockingQueue`
2. **Rate Limiting**: Before processing, each consumer acquires a permit from the `RateLimiterService` (max 2 concurrent orders)
3. **Order Processing**:
   - Consumer takes an order from the queue
   - Updates status to PROCESSING
   - Checks inventory and reduces stock if available
   - Processes payment (simulated with 500ms delay)
   - Updates status to COMPLETED or FAILED
   - Releases the rate limiter permit
4. **Status Tracking**: All order status changes are recorded in `OrderStatusService`

## Requirements

- Java 8 or higher
- No external dependencies required (uses only Java standard library)

## Compilation and Execution

### Compile the project:

```bash
cd OrderSystem/src
javac Main.java model/*.java service/*.java
```

### Run the application:

```bash
java Main
```

## Sample Output

```
Order placed: 1
Order placed: 2
Order placed: 3
Order placed: 4
Payment successful for order 1
Payment successful for order 2
Payment successful for order 3
Payment successful for order 4
Current Inventory: {Laptop=-12, Phone=5}
Order Statuses: {1=COMPLETED, 2=COMPLETED, 3=COMPLETED, 4=COMPLETED}
```

## Configuration

You can modify the following parameters in `Main.java`:

- **Queue Size**: `new ArrayBlockingQueue<>(10)` - Maximum orders in queue
- **Thread Pool Size**: `Executors.newFixedThreadPool(3)` - Number of consumer threads
- **Rate Limit**: `new RateLimiterService(2)` - Maximum concurrent order processing
- **Processing Time**: Adjust `Thread.sleep(4000)` for observation duration

## Key Concepts Demonstrated

- **Thread Safety**: Multiple threads accessing shared resources safely
- **Producer-Consumer Pattern**: Decoupling order creation from processing
- **Resource Pooling**: Limited thread pool for efficient resource usage
- **Rate Limiting**: Controlling system load with semaphores
- **Graceful Shutdown**: Proper thread pool termination

## Future Enhancements

- Add persistent storage (database integration)
- Implement order cancellation feature
- Add real payment gateway integration
- Introduce order priority queue
- Add comprehensive logging framework
- Implement retry mechanism for failed orders
- Add metrics and monitoring
- Implement distributed processing with message queues (e.g., RabbitMQ, Kafka)

## License

This project is open source and available for educational purposes.
