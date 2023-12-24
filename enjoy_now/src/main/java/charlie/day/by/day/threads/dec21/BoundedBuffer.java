package charlie.day.by.day.threads.dec21;

/**
 *
 To demonstrate state-dependent classes using low-level synchronization mechanisms in Java,
 let's consider a real-life example of a bounded buffer, also known as a blocking queue.
 This is a common scenario in concurrent programming where a data structure is shared between producers (who add items to the buffer)
 and consumers (who remove items from it).
 The buffer has a maximum capacity, so the producers must wait if the buffer is full, and consumers must wait if the buffer is empty.
 * @param <T>
 */
public class BoundedBuffer<T> {
    private final T[] buffer;
    private int count = 0, in = 0, out = 0;

    @SuppressWarnings("unchecked")
    public BoundedBuffer(int size) {
        buffer = (T[]) new Object[size];
    }

    public synchronized void put(T item) throws InterruptedException {
        while (count == buffer.length) {
            // Buffer is full; wait for space to become available
            wait();
        }
        buffer[in] = item;
        in = (in + 1) % buffer.length;
        count++;
        notifyAll(); // Notify all waiting threads that the state has changed
    }

    public synchronized T take() throws InterruptedException {
        while (count == 0) {
            // Buffer is empty; wait for an item to become available
            wait();
        }
        T item = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        notifyAll(); // Notify all waiting threads that the state has changed
        return item;
    }

    public static void main(String[] args) {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(10);

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
