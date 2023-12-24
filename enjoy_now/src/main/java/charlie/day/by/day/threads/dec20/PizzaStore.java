package charlie.day.by.day.threads.dec20;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

class Pizza {
    private final long creationTime;

    public Pizza() {
        this.creationTime = System.currentTimeMillis();
    }

    public boolean isUnavailable() {
        return (System.currentTimeMillis() - creationTime) > 120000; // 120 seconds
    }
}

class Producer implements Runnable {
    private final ConcurrentLinkedQueue<Pizza> queue;
    private final long interval;
    private final ReentrantLock lock;

    public Producer(ConcurrentLinkedQueue<Pizza> queue, long interval, ReentrantLock lock) {
        this.queue = queue;
        this.interval = interval;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(interval);

                lock.lock();
                try {
                    if (queue.size() < 100) {
                        queue.add(new Pizza());
                        System.out.println("Produced pizza. Queue size: " + queue.size());
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable {
    private final ConcurrentLinkedQueue<Pizza> queue;
    private final Random random = new Random();

    public Consumer(ConcurrentLinkedQueue<Pizza> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(random.nextInt(6000) + 10000); // Random sleep between 10 and 15 seconds
                Pizza pizza = queue.poll();
                if (pizza != null) {
                    if(! pizza.isUnavailable()){
                        System.out.println("Consumed pizza. Queue size: " + queue.size());
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class StatusChecker implements Runnable {
    private final ConcurrentLinkedQueue<Pizza> queue;

    public StatusChecker(ConcurrentLinkedQueue<Pizza> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Check every second
                queue.removeIf(Pizza::isUnavailable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class PizzaStore {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Pizza> queue = new ConcurrentLinkedQueue<>();
        ReentrantLock lock = new ReentrantLock();

        Thread producer1 = new Thread(new Producer(queue, 2000, lock)); // 2 seconds
        Thread producer2 = new Thread(new Producer(queue, 4000, lock)); // 4 seconds
        Thread consumer1 = new Thread(new Consumer(queue));
        Thread consumer2 = new Thread(new Consumer(queue));
        Thread statusChecker = new Thread(new StatusChecker(queue));

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
        statusChecker.start();
    }
}
