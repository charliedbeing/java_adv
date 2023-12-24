package charlie.day.by.day.threads.dec21;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * To improve the performance of the previous monitor pattern example, especially in scenarios where high concurrency is expected, we can use explicit locks from the java.util.concurrent.locks package. Explicit locks offer more fine-grained control over locking and unlocking, which can lead to better performance compared to synchronized methods or blocks.
 *
 * In this improved example, we will use a ReentrantLock. This type of lock allows us to explicitly define the lock and unlock operations. It also offers additional features, such as the ability to interrupt a thread waiting for a lock, or to time out while waiting for a lock.
 *
 * Explanation
 * ReentrantLock: An instance of ReentrantLock is used to control access to the shared count variable.
 * Locking and Unlocking: Each method (increment, decrement, getCount) explicitly acquires the lock at the beginning and releases it in the finally block to ensure the lock is released even if an exception occurs.
 * Thread Safety: The explicit lock ensures that only one thread can modify the counter at a time, maintaining thread safety.
 * Key Concepts
 * Explicit Lock Control: ReentrantLock provides more explicit control over when to lock and unlock, potentially leading to more efficient use of resources.
 * Try-Finally Block: Ensuring that unlock is called in a finally block is crucial to prevent deadlocks in case of exceptions.
 * Scalability and Performance: Explicit locks can offer better scalability and performance in high-concurrency environments compared to intrinsic locks (synchronized methods/blocks).
 * Note
 * While explicit locks like ReentrantLock provide more control and can improve performance in some scenarios, they also require more careful management to avoid deadlocks and other concurrency issues.
 * The choice between intrinsic locks (synchronized) and explicit locks (ReentrantLock) should be based on specific use cases and performance requirements.
 */
 class Counter2 {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    // Increments the count
    public void increment() {
        lock.lock();
        try {
            count++;
            System.out.println("Count incremented to: " + count);
        } finally {
            lock.unlock();
        }
    }

    // Decrements the count
    public void decrement() {
        lock.lock();
        try {
            count--;
            System.out.println("Count decremented to: " + count);
        } finally {
            lock.unlock();
        }
    }

    // Gets the current count
    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}

public class ImprovedMonitorPatternDemo {

    public static void main(String[] args) {
        Counter2 counter = new Counter2();

        // Thread 1 - incrementing the counter
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.increment();
            }
        }).start();

        // Thread 2 - decrementing the counter
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.decrement();
            }
        }).start();
    }
}
