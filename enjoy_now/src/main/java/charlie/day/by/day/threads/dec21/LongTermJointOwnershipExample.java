package charlie.day.by.day.threads.dec21;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 Envisioning long-term joint ownership in a Java context can be interpreted as multiple threads sharing access to a common resource over the duration of their existence, with no single thread having exclusive ownership. A typical scenario for this is a shared configuration object or a shared cache that all threads can read from and write to over the application's lifetime.

 To demonstrate this, let's create an example where multiple threads share access to a common resource, such as a shared configuration object. We'll use ReadWriteLock to allow concurrent reads while maintaining thread safety during writes.

 Explanation
 SharedConfiguration: This class represents the shared resource. It maintains a configuration map and uses a ReadWriteLock to manage access.
 Write Operation: The updateConfig method updates the configuration. It acquires a write lock to ensure exclusive access during the update.
 Read Operation: The getConfig method reads a configuration value. It acquires a read lock, allowing concurrent reads but blocking writes.
 Threads: The configUpdater thread updates the configuration, while the configReader thread reads the configuration. Both threads operate on the same SharedConfiguration instance.
 Key Concepts
 Joint Ownership: Multiple threads share access to the SharedConfiguration object. This shared access is long-term and persists for the lifetime of the threads.
 Thread Safety: ReadWriteLock allows multiple threads to read the configuration concurrently but ensures exclusive access when writing to prevent data corruption.
 Concurrent Access: The example demonstrates how concurrent reads and exclusive writes can be managed in a multi-threaded environment.
 In this scenario, the concept of "long-term joint ownership" is demonstrated by the shared usage of the SharedConfiguration object by multiple threads,
 ensuring that the resource is concurrently accessible and modifications are safely managed.
 */

public class LongTermJointOwnershipExample {

    static class SharedConfiguration {
        private final Map<String, String> configMap = new HashMap<>();
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        // Method to update configuration - Write operation
        public void updateConfig(String key, String value) {
            lock.writeLock().lock();
            try {
                configMap.put(key, value);
                System.out.println("Configuration updated: " + key + " = " + value);
            } finally {
                lock.writeLock().unlock();
            }
        }

        // Method to read configuration - Read operation
        public String getConfig(String key) {
            lock.readLock().lock();
            try {
                return configMap.get(key);
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    public static void main(String[] args) {
        SharedConfiguration sharedConfig = new SharedConfiguration();

        // Thread for updating the configuration
        Thread configUpdater = new Thread(() -> {
            sharedConfig.updateConfig("DatabaseURL", "jdbc:mysql://localhost:3306/mydb");
            sharedConfig.updateConfig("MaxConnections", "100");
        });

        // Thread for reading the configuration
        Thread configReader = new Thread(() -> {
            try {
                Thread.sleep(1000); // Delay to ensure update happens first
                System.out.println("DatabaseURL: " + sharedConfig.getConfig("DatabaseURL"));
                System.out.println("MaxConnections: " + sharedConfig.getConfig("MaxConnections"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        configUpdater.start();
        configReader.start();
    }
}
