package charlie.day.by.day.threads.basic;

import java.util.Random;

class PizzaProducer extends Thread {
    private static final int PRODUCTION_RATE_MILLIS = 10000; // 10 seconds
    private PizzaStore pizzaStore;

    public PizzaProducer(PizzaStore pizzaStore) {
        this.pizzaStore = pizzaStore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(PRODUCTION_RATE_MILLIS); // Producing pizza every 10 seconds
                pizzaStore.producePizza();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class PizzaConsumer extends Thread {
    private static final int MIN_CONSUMPTION_TIME = 30000; // 30 seconds
    private static final int MAX_CONSUMPTION_TIME = 60000; // 60 seconds
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
    private int pizzaCount = 0;

    public synchronized void producePizza() throws InterruptedException {
        pizzaCount++;
        System.out.println("Produced a pizza. Total pizzas: " + pizzaCount);
        notifyAll(); // Notify waiting consumers
    }

    public synchronized void consumePizza() throws InterruptedException {
        while (pizzaCount == 0) {
            wait(); // Wait if there are no pizzas
        }

        pizzaCount--;
        System.out.println("Consumed a pizza. Remaining pizzas: " + pizzaCount);
    }
}

public class PizzaDemo {
    public static void main(String[] args) {
        PizzaStore pizzaStore = new PizzaStore();
        PizzaProducer producer = new PizzaProducer(pizzaStore);
        PizzaConsumer consumer1 = new PizzaConsumer(pizzaStore);
        PizzaConsumer consumer2 = new PizzaConsumer(pizzaStore);

        producer.start();
        consumer1.start();
        consumer2.start();
    }
}
