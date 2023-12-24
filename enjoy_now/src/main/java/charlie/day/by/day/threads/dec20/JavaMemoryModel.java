package charlie.day.by.day.threads.dec20;

import java.util.ArrayList;
import java.util.Random;

class Person {

    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void setName(String name){
        this.name = this.name + name;
    }

    @Override
    public String toString() {
        return name;
    }
}


class StackMemoryExample {

    private Person guard;

    private  int count=0;
    private final ThreadLocal<ArrayList<String>> threadLocalVariable = new ThreadLocal<>();
    public StackMemoryExample(){
        this.guard = new Person("AutoMan");
    }

    private synchronized void printNumber() {

        int localVar = 100; // This is a local variable stored in the stack memory
        System.out.println("Local variable value: " + localVar);
        localVar +=1;
        System.out.println("add to " + localVar);
        Random random = new Random();
        int randomNumber =  random.nextInt();
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add(String.valueOf(randomNumber));

        this.threadLocalVariable.set(list);
        this.threadLocalVariable.get().add("final");

        Person person = new Person("John" + randomNumber);
        int identityHashCode = System.identityHashCode(person);
        System.out.println("Person hashcode address:" +identityHashCode);
        System.out.println(person);
        System.out.println("-------------------");
        this.guard.setName(String.valueOf(identityHashCode));
        System.out.println(this.guard);
        System.out.println("++++++++++++++");
        System.out.println(System.identityHashCode(this.guard));
        System.out.println("===test thread local===============");
        for(int i=0;i<this.threadLocalVariable.get().size();i++){
            System.out.print(this.threadLocalVariable.get().get(i));
        }

        System.out.println();

    }

    private synchronized void add(){
        for(int i=0;i<1000000;i++){
            count= count+1;
        }
        System.out.println("count" + count);
    }
    public static void main(String[] args) {
        StackMemoryExample example = new StackMemoryExample();
        // Thread 1
//        new Thread(example::printNumber).start();
//        System.out.println("thread saperation---------------");
//        // Thread 2
//        new Thread(example::printNumber).start();


        new Thread(example::add).start();
        System.out.println("thread saperation---------------");
        // Thread 2
        new Thread(example::add).start();
    }
}
