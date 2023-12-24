package charlie.day.by.day.threads;

import java.util.*;

class PizzaProducer extends Thread {
    private static final int PRODUCTION_RATE_MILLIS_1 = 300; // 3 seconds
    private static final int PRODUCTION_RATE_MILLIS_2 = 1000;  // 1 seconds
    private PizzaStore pizzaStore;

    public PizzaProducer(PizzaStore pizzaStore) {
        this.pizzaStore = pizzaStore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(PRODUCTION_RATE_MILLIS_1); // Producing pizza every 10 seconds
                pizzaStore.producePizza(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class FasterPizzaProducer extends Thread {
    private static final int PRODUCTION_RATE_MILLIS = 100; // 1 seconds
    private PizzaStore pizzaStore;

    public FasterPizzaProducer(PizzaStore pizzaStore) {
        this.pizzaStore = pizzaStore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(PRODUCTION_RATE_MILLIS); // Producing pizza every 5 seconds
                pizzaStore.producePizza(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class PizzaConsumer extends Thread {
    private static final int MIN_CONSUMPTION_TIME = 400; // 4 seconds
    private static final int MAX_CONSUMPTION_TIME = 500; // 5 seconds
    private PizzaStore pizzaStore;
    private Random random = new Random();

    public PizzaConsumer(PizzaStore pizzaStore) {
        this.pizzaStore = pizzaStore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int consumptionTime = random.nextInt(MAX_CONSUMPTION_TIME - MIN_CONSUMPTION_TIME) + MIN_CONSUMPTION_TIME;
                sleep(consumptionTime);
                pizzaStore.consumePizza();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class PizzaStore {
    private Queue<Pizza> availableQueue = new LinkedList<>();
    private Queue<Pizza> overtimeQueue = new LinkedList<>();

//    public PizzaStore(){
//        startAvailabilityCheckTask();
//    }
    public synchronized void producePizza(int producerId) throws InterruptedException {
        Pizza pizza = new Pizza(producerId);
        availableQueue.add(pizza);
        System.out.println("Producer " + producerId + " produced a pizza. Total pizzas: " + availableQueue.size());

        notifyAll(); // Notify waiting consumers
    }

    public synchronized void consumePizza() throws InterruptedException {
        while (availableQueue.isEmpty()) {
            wait(); // Wait if there are no pizzas
        }

        Pizza pizza = availableQueue.poll();
        System.out.println("Consumer consumed a pizza. Total pizzas remaining: " + availableQueue.size());

//        pizza.consume();
        if (! pizza.isAvailable()) {
            overtimeQueue.add(pizza);
            System.out.println("Pizza moved to overtime queue. Total pizzas in overtime: " + overtimeQueue.size());
        }
    }
    private void startAvailabilityCheckTask() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAvailabilityAndRemove();
            }
        }, 0, 600); // Run every 30 seconds
    }

    private void checkAvailabilityAndRemove() {
        synchronized (this) {
            Iterator<Pizza> iterator = availableQueue.iterator();
            while (iterator.hasNext()) {
                Pizza pizza = iterator.next();
                if (!pizza.isAvailable()) {
                    iterator.remove();
                    System.out.println("Removed unavailable pizza from the available queue.");
                }
            }
        }
    }

}

class Pizza {
    private int savingTime = 0;
    private int producerId;

    private boolean available = true;

    public Pizza(int producerId) {
        this.producerId = producerId;
        consume();
    }

    public void consume() {
        // Simulate pizza consumption by increasing saving time
        while (savingTime <= 120) {
            try {
                Thread.sleep(100); // 1 second
                savingTime++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
    }

    public int getSavingTime() {
        return savingTime;
    }

    public boolean isAvailable(){
        return available;
    }
}

public class PizzaDemoAdv {
    public static void main(String[] args) {
        PizzaStore pizzaStore = new PizzaStore();
        PizzaProducer producer1 = new PizzaProducer(pizzaStore);
        FasterPizzaProducer producer2 = new FasterPizzaProducer(pizzaStore);
        PizzaConsumer consumer1 = new PizzaConsumer(pizzaStore);
        PizzaConsumer consumer2 = new PizzaConsumer(pizzaStore);

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
    }

    public static void test(){
        PizzaStore pizzaStore = new PizzaStore();
        PizzaProducer producer1 = new PizzaProducer(pizzaStore);
        FasterPizzaProducer producer2 = new FasterPizzaProducer(pizzaStore);
        PizzaConsumer consumer1 = new PizzaConsumer(pizzaStore);
        PizzaConsumer consumer2 = new PizzaConsumer(pizzaStore);

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
    }
}

