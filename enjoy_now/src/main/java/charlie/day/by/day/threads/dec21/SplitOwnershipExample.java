package charlie.day.by.day.threads.dec21;

import java.util.HashMap;
import java.util.Map;

/**
 * In Java, "split ownership" can be demonstrated by dividing responsibility for different parts of a collection among multiple entities. This concept can be illustrated using a Map, where different threads or components are responsible for their own specific portion of the map.
 *
 * Let's consider an example with a HashMap where different threads are responsible for managing different subsets of the map. Each thread only adds, updates, or reads key-value pairs within its assigned range.
 *
 *
 * Explanation
 * Task Class: Now includes three phases:
 * Adding: Each thread adds key-value pairs in its range to the map.
 * Updating: Each thread updates the values for the keys in its range.
 * Reading: Each thread reads and prints the values for the keys in its range.
 * Threads: t1 and t2 perform add, update, and read operations on different portions of the map.
 * Key Concepts
 * Concurrent Operations: The threads perform add, update, and read operations on the map concurrently but within their designated ranges.
 * Split Ownership: Each thread operates independently on a different part of the map, demonstrating split ownership.
 * Thread Safety: As each thread works on a distinct part of the map, there is no conflict. However, if multiple threads were to modify the same keys, synchronization would be necessary.
 * Note
 * This example demonstrates split ownership in a scenario where each thread operates on a separate part of the map.
 * In real-world applications, especially when shared data might be accessed or modified by multiple threads simultaneously,
 * using thread-safe collections or implementing appropriate synchronization mechanisms is crucial to ensure data integrity and prevent concurrency-related issues.
 *
 */

public class SplitOwnershipExample {

    static class Task implements Runnable {
        private final Map<Integer, String> sharedMap;
        private final int startRange;
        private final int endRange;

        public Task(Map<Integer, String> sharedMap, int startRange, int endRange) {
            this.sharedMap = sharedMap;
            this.startRange = startRange;
            this.endRange = endRange;
        }

        @Override
        public void run() {
            // Adding entries
            for (int i = startRange; i <= endRange; i++) {
                sharedMap.put(i, "Value" + i);
                System.out.println(Thread.currentThread().getName() + " added: Key" + i + ", Value" + i);
            }

            // Updating entries
            for (int i = startRange; i <= endRange; i++) {
                sharedMap.put(i, "UpdatedValue" + i);
                System.out.println(Thread.currentThread().getName() + " updated: Key" + i + ", UpdatedValue" + i);
            }

            // Reading entries
            for (int i = startRange; i <= endRange; i++) {
                System.out.println(Thread.currentThread().getName() + " read: Key" + i + ", " + sharedMap.get(i));
            }
        }
    }

    public static void main(String[] args) {
        Map<Integer, String> sharedMap = new HashMap<>();

        Thread t1 = new Thread(new Task(sharedMap, 1, 5), "Thread-1");
        Thread t2 = new Thread(new Task(sharedMap, 6, 10), "Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final shared map: " + sharedMap);
    }
}
