package charlie.day.by.day.threads.dec21;

import java.util.ArrayList;
import java.util.List;

/**
 * Instance confinement is a technique used to ensure thread safety by encapsulating and confining an object within another class, so that the object is not exposed to multiple threads. This can be achieved by keeping the object private and not allowing direct access to it from outside the class.
 *
 * A classic example of instance confinement is to encapsulate a non-thread-safe object within a thread-safe class. Let¡¯s demonstrate this with a class that encapsulates an ArrayList, which is not thread-safe, and provides thread-safe methods to manipulate it.
 *
 * Explanation
 * Encapsulation: The ArrayList is kept private within the ThreadSafeListWrapper class, preventing external access.
 * Synchronized Methods: Methods add, remove, and get are synchronized, ensuring that only one thread can execute them at a time. This prevents concurrent modifications that could lead to inconsistent states.
 * Thread Safety: By confining the ArrayList instance within a thread-safe class and providing controlled access through synchronized methods, we ensure that the ArrayList is safe to use in a multithreaded environment.
 * Key Concepts
 * Instance Confinement: The non-thread-safe ArrayList is confined within the ThreadSafeListWrapper class, ensuring that all interactions with the ArrayList are controlled and thread-safe.
 * Controlled Access: External access to the ArrayList is only possible through the thread-safe methods provided by ThreadSafeListWrapper.
 * Note
 * Performance Consideration: While synchronizing each method ensures thread safety, it can impact performance due to increased contention. Depending on the use case, other concurrency mechanisms like ReadWriteLock might be more efficient.
 * Immutability and Confinement: Another approach to achieve thread safety is to use immutable objects. Immutability, combined with instance confinement, is a powerful way to ensure that objects remain thread-safe.
 * @param <T>
 */
public class ThreadSafeListWrapper<T> {
    private final List<T> list = new ArrayList<>(); // Non-thread-safe list

    // Adds an item to the list in a thread-safe manner
    public synchronized void add(T item) {
        list.add(item);
    }

    // Removes an item from the list in a thread-safe manner
    public synchronized boolean remove(T item) {
        return list.remove(item);
    }

    // Gets an item from the list in a thread-safe manner
    public synchronized T get(int index) {
        return list.get(index);
    }

    // Other thread-safe methods can be added as needed

    public static void main(String[] args) {
        ThreadSafeListWrapper<String> threadSafeList = new ThreadSafeListWrapper<>();

        // Thread 1: Adds items to the list
        new Thread(() -> {
            threadSafeList.add("Item 1");
            threadSafeList.add("Item 2");
        }).start();

        // Thread 2: Removes an item from the list
        new Thread(() -> {
            threadSafeList.remove("Item 1");
        }).start();
    }
}




