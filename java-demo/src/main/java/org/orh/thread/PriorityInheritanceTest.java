package org.orh.thread;
public class PriorityInheritanceTest {
    public static void main(String[] args) {
        System.out.printf("main thread begin priority= %d\n", Thread.currentThread().getPriority());
        Thread.currentThread().setPriority(6);
        System.out.printf("main thread end priority= %d\n", Thread.currentThread().getPriority());
       
        Thread t1 = new Thread(() -> {
            System.out.printf("t1 run priority= %d\n", Thread.currentThread().getPriority());

            Thread t2 = new Thread(() -> {
                System.out.printf("t2 run priority= %d\n", Thread.currentThread().getPriority());
            });
            t2.start();
        });
        t1.start();
    }

}
