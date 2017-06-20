package org.orh.thread.group;

public class GroupAddThread {
    public static void main(String[] args) {
        ThreadGroup testThreadGroup = new ThreadGroup("测试任务线程组");

        new Thread(testThreadGroup, () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " 开始执行...");
            sleep(2000);
            System.out.println(threadName + " 执行结束.");
        }, "thread-test-1").start();

        new Thread(testThreadGroup, () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " 开始执行...");
            sleep(1000);
            System.out.println(threadName + " 执行结束.");
        }, "thread-test-2").start();

        System.out.println(testThreadGroup.getName() + "-活动的线程数为：" + testThreadGroup.activeCount());
        sleep(1200);
        System.out.println(testThreadGroup.getName() + "-活动的线程数为：" + testThreadGroup.activeCount());
        sleep(1200);
        System.out.println(testThreadGroup.getName() + "-活动的线程数为：" + testThreadGroup.activeCount());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
