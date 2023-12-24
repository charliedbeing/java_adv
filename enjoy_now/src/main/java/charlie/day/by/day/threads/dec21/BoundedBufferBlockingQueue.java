package charlie.day.by.day.threads.dec21;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Explanation
 * Blocking Queue: ArrayBlockingQueue is used to store the items. It handles all synchronization and thread coordination internally.
 * put Method: Adds an item to the queue. If the queue is full, the thread will block until space becomes available.
 * take Method: Removes and returns an item from the queue. If the queue is empty, the thread will block until an item is available.
 * Producer and Consumer Threads: Separate threads for producing and consuming items demonstrate the interaction with the buffer.
 * Key Concepts
 * Simplified Concurrency: By using ArrayBlockingQueue, the complexity of manually handling wait, notify, and synchronization is eliminated.
 * Thread Safety: The blocking queue is inherently thread-safe, so no additional synchronization is needed.
 * Efficient Coordination: The queue efficiently manages waiting and notification for threads trying to add or remove elements.
 * Note
 * Using ArrayBlockingQueue simplifies the implementation of producer-consumer scenarios significantly and is recommended for most use cases where a bounded buffer is needed in a concurrent environment.
 */
public class BoundedBufferBlockingQueue {
    private final BlockingQueue<Integer> queue;

    public BoundedBufferBlockingQueue(int size) {
        queue = new ArrayBlockingQueue<>(size);
    }

    public void put(int item) throws InterruptedException {
        queue.put(item);
        System.out.println("Produced: " + item);
    }

    public int take() throws InterruptedException {
        int item = queue.take();
        System.out.println("Consumed: " + item);
        return item;
    }

    public static void main(String[] args) {
        BoundedBufferBlockingQueue buffer = new BoundedBufferBlockingQueue(10);

        // Producer thread
        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // Consumer thread
        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    buffer.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
