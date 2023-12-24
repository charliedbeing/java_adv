package charlie.day.by.day.threads.dec21;

import java.util.concurrent.Semaphore;

/**
 * In Java, demonstrating the concept of a "short-term loan" can be interpreted as temporarily sharing a resource or object between entities (such as threads) for a brief period, then reclaiming it. This concept is common in concurrency, where resources are often shared temporarily among threads.
 *
 * To illustrate this, let's create an example where a thread (the "borrower") temporarily uses a resource (like a database connection or a file), represented by an object, and then releases it back for others to use. We'll use a Semaphore to represent the resource being loaned.
 *
 * Explanation
 * SharedResource: Represents the resource to be shared. It has a method useResource to simulate the use of the resource.
 * Semaphore: Controls access to the resource. It's initialized with one permit, indicating that only one thread can use the resource at a time.
 * Runnable Task: Defines the work to be done by each thread, including waiting to acquire the resource, using it, and then releasing it.
 * Threads: t1 and t2 are two threads that represent borrowers of the resource. They both attempt to acquire the resource, use it, and then release it.
 * Key Concepts
 * Resource Sharing: The semaphore ensures that only one thread can access the shared resource at a time, mimicking the concept of a short-term loan.
 * Fairness: Threads are queued to acquire the resource, ensuring fair access.
 * Resource Loan: Each thread "borrows" the resource for a short time (simulated by Thread.sleep(1000)) and then "returns" it, making it available for others.
 * This example demonstrates how a resource can be shared among threads in a controlled manner, where each thread gets temporary access to the resource, akin to a short-term loan.
 */
public class ShortTermLoanExample {

    static class SharedResource {
        private final String name;

        SharedResource(String name) {
            this.name = name;
        }

        void useResource(String user) {
            System.out.println(user + " is using " + name);
            // Simulating resource usage
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(user + " has finished using " + name);
        }
    }

    public static void main(String[] args) {
        SharedResource resource = new SharedResource("SharedResource");
        Semaphore semaphore = new Semaphore(1); // Only one permit, representing one resource

        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is requesting the resource");
                semaphore.acquire(); // Acquire the resource
                resource.useResource(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release(); // Release the resource
            }
        };

        // Creating multiple threads to demonstrate resource sharing
        Thread t1 = new Thread(task, "Thread 1");
        Thread t2 = new Thread(task, "Thread 2");

        t1.start();
        t2.start();
    }
}
