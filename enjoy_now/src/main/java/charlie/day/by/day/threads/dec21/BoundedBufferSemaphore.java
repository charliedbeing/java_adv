package charlie.day.by.day.threads.dec21;

import java.util.concurrent.Semaphore;

/**
 Explanation
 Semaphores:
 items: Represents the count of items in the buffer.
 spaces: Represents the available space in the buffer.
 mutex: Ensures mutual exclusion when accessing the buffer.
 put Method: Waits for a free space (spaces.acquire()), safely adds an item to the buffer, and then signals an increase in the item count (items.release()).
 take Method: Waits for an available item (items.acquire()), safely removes an item from the buffer, and then signals an increase in the available space (spaces.release()).
 Key Concepts
 Semaphore for Item Count and Space: By using semaphores, we can directly express the dependency of the put and take operations on the available items and spaces in the buffer.
 Mutual Exclusion: The mutex semaphore is used to ensure that only one thread can access the buffer at a time, preserving data integrity.
 Note
 Semaphores provide a clear and concise way to express the conditions for waiting and signaling in producer-consumer problems, making the code easier to understand and maintain.
 * @param <T>
 */
public class BoundedBufferSemaphore<T> {
    private final T[] buffer;
    private int in = 0, out = 0;
    private final Semaphore items; // Items in the buffer
    private final Semaphore spaces; // Available spaces in the buffer
    private final Semaphore mutex; // For mutual exclusion on buffer access

    @SuppressWarnings("unchecked")
    public BoundedBufferSemaphore(int size) {
        buffer = (T[]) new Object[size];
        items = new Semaphore(0);
        spaces = new Semaphore(size);
        mutex = new Semaphore(1);
    }

    public void put(T item) throws InterruptedException {
        spaces.acquire(); // Acquire a space
        mutex.acquire(); // Acquire exclusive access
        try {
            buffer[in] = item;
            in = (in + 1) % buffer.length;
        } finally {
            mutex.release(); // Release exclusive access
        }
        items.release(); // Increase the items count
    }

    public T take() throws InterruptedException {
        items.acquire(); // Acquire an item
        mutex.acquire(); // Acquire exclusive access
        try {
            T item = buffer[out];
            out = (out + 1) % buffer.length;
            return item;
        } finally {
            mutex.release(); // Release exclusive access
            spaces.release(); // Increase the available spaces
        }

    }

    public static void main(String[] args) {
        BoundedBufferSemaphore<Integer> buffer = new BoundedBufferSemaphore<>(10);

        // Producer thread
        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    buffer.put(i);
                    System.out.println("Produced: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // Consumer thread
        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    System.out.println("Consumed: " + buffer.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

