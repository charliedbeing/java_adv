package charlie.day.by.day.threads.dec21;

 class Counter {
    private int count = 0;

    // Increments the count in a synchronized method
    public synchronized void increment() {
        count++;
        System.out.println("Count incremented to: " + count);
    }

    // Decrements the count in a synchronized method
    public synchronized void decrement() {
        count--;
        System.out.println("Count decremented to: " + count);
    }

    // Gets the current count in a synchronized method
    public synchronized int getCount() {
        return count;
    }
}

public class MonitorPatternDemo {

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
