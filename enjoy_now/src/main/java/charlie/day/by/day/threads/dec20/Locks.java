package charlie.day.by.day.threads.dec20;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class AddExample{
    private final Lock lock = new ReentrantLock();

    private int count=0;

    public void add(){
        lock.lock();
        try {
            for(int i=0;i<1000000;i++){
                count = count+1;
            }
            System.out.println("=============" + this.count);
        }finally {
            lock.unlock();
        }

    }



}
 class TimedLockExample {

    private final Lock lock = new ReentrantLock();

    public void attemptLock(String threadName) {
        try {
            // Trying to acquire the lock for 2 seconds
            if (lock.tryLock(2, TimeUnit.SECONDS)) {
                try {
                    System.out.println(threadName + " acquired the lock.");
                    // Simulate some processing
                    Thread.sleep(1000);
                } finally {
                    lock.unlock();
                    System.out.println(threadName + " released the lock.");
                }
            } else {
                System.out.println(threadName + " could not acquire the lock.");
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " was interrupted.");
        }
    }


}


class AuctionItem {
    private final Lock lock = new ReentrantLock();

    // Method to simulate placing a bid
    public void placeBid(int bidAmount, Thread biddingThread) {
        try {
            System.out.println(biddingThread.getName() + " attempting to place bid.");
            // Attempt to lock interruptibly
            lock.lockInterruptibly();
            try {
                // Simulating bid operation
                System.out.println(biddingThread.getName() + " placed a bid of $" + bidAmount);
                Thread.sleep(2000); // Simulate time taken to place a bid
            } finally {
                lock.unlock(); // Ensure the lock is released
                System.out.println(biddingThread.getName() + " finished bidding.");
            }
        } catch (InterruptedException e) {
            System.out.println(biddingThread.getName() + " bid was interrupted or cancelled.");
        }
    }
}


 class ProducerConsumerExample {

    private static final int CAPACITY = 5;
    private final Queue<Integer> buffer = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    // Producer Thread
    class Producer implements Runnable {
        public void run() {
            int value = 0;
            while (true) {
                lock.lock();
                try {
                    while (buffer.size() == CAPACITY) {
                        // Wait for the buffer to have space
                        notFull.await();
                    }
                    buffer.add(value);
                    System.out.println("Produced: " + value);
                    value++;
                    // Signal the consumer that there is data in the buffer
                    notEmpty.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    // Consumer Thread
    class Consumer implements Runnable {
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (buffer.isEmpty()) {
                        // Wait for the buffer to have data
                        notEmpty.await();
                    }
                    int value = buffer.poll();
                    System.out.println("Consumed: " + value);
                    // Signal the producer that there is space in the buffer
                    notFull.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }


}



public class Locks {
    public static void main(String[] args)  {
//        multiConditionVariables();

         addDemo();
    }
    public static void addDemo() {
        AddExample example = new AddExample();

        Thread t1 = new Thread(() -> example.add());
        Thread t2 = new Thread(() -> example.add());

        t1.start();
        t2.start();
    }

    public static void tryLockDemo() {
        TimedLockExample example = new TimedLockExample();

        Thread t1 = new Thread(() -> example.attemptLock("Thread 1"));
        Thread t2 = new Thread(() -> example.attemptLock("Thread 2"));

        t1.start();
        t2.start();
    }
    public static void interruptDemo() throws InterruptedException {
        AuctionItem item = new AuctionItem();

        // Thread simulating a user bidding
        Thread bidder1 = new Thread(() -> item.placeBid(100, Thread.currentThread()), "Bidder1");

        // Start the bidding
        bidder1.start();

        // Simulate a scenario where the bid is interrupted (e.g., auction ends or user cancels)
        Thread.sleep(1000); // Wait for 1 second before interrupting
        System.out.println("Auction ended! Interrupting the bid.");
        bidder1.interrupt();
    }
    public static void multiConditionVariables() {

        ProducerConsumerExample example = new ProducerConsumerExample();
        Thread producerThread = new Thread(example.new Producer());
        Thread consumerThread = new Thread(example.new Consumer());

        producerThread.start();
        consumerThread.start();
    }

}
