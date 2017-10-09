package org.orh.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorCase {

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            executor.execute(new Task()); // execute 不会被阻塞
            System.out.println("submit: " + i); // 相继输出
        }
        // 全部任务执行完，main方法不会结束，因为线程池中的线程只是结束了工作 进入了 idle 状态
    }

    static class Task implements Runnable {
        public void run() {
            sleep(5000); // 模拟异步查询数据库，睡眠5s
            System.out.println(Thread.currentThread().getName());
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
