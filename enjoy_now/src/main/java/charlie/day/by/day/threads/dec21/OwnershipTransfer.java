package charlie.day.by.day.threads.dec21;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Transferring ownership of an object in Java typically involves ensuring that an object,
 * once owned by one entity (like a thread or component), is safely passed to another.
 * A common scenario in concurrent programming is transferring data between threads.
 * One way to demonstrate this is through the use of a BlockingQueue, where one thread produces data and another consumes it,
 * effectively transferring the ownership of the data items.
 *
 * Explanation
 * Data Class: Represents the data object to be transferred.
 * BlockingQueue: Serves as the medium for transferring ownership of Data objects from the producer to the consumer.
 * Producer Thread: Produces Data objects and puts them into the queue, effectively transferring ownership of these objects to whoever takes them from the queue.
 * Consumer Thread: Takes Data objects from the queue, effectively taking ownership of them.
 * Key Concepts
 * Safe Transfer: The BlockingQueue ensures that the transfer of data between threads is thread-safe, avoiding concurrency issues.
 * Ownership Semantics: Once a Data object is taken from the queue by the consumer, the producer no longer has access to it, demonstrating the transfer of ownership.
 * Note
 * In this example, "ownership" is more about access and control over the data objects rather than memory management,
 * which is handled by Java's garbage collector. The focus is on ensuring that once an object is passed from one thread to another,
 * the originating thread relinquishes control over that object, and the receiving thread becomes its sole manager.
 */
public class OwnershipTransfer {

    static class Data {
        private final String content;

        public Data(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Data> queue = new ArrayBlockingQueue<>(10);

        // Producer Thread
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                Data data = new Data("Data " + i);
                try {
                    queue.put(data);
                    System.out.println("Produced: " + data.getContent());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Consumer Thread
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Data data = queue.take();
                    System.out.println("Consumed: " + data.getContent());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
