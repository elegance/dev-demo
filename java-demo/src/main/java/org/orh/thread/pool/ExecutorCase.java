package org.orh.thread.pool;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorCase {

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        
        for (int i = 0; i < 20; i++) {
            executor.execute(new Task()); // execute 不会被阻塞
            System.out.println("submit: " + i); // 相继输出
        }
    }

    static class Task implements Runnable {
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        }
    }
}
