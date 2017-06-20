package org.orh.thread;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NotifyOne {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedDeque<String> queue  = new ConcurrentLinkedDeque<String>();
        queue.add("a");
        queue.add("b");
        queue.add("c");
        
        Object lock = new Object();
        Service service = new Service();

        Thread t1 = new Thread(() -> {
            service.testMethod(lock);
        }, "t1");
        Thread t2 = new Thread(() -> {
            service.testMethod(lock);
        }, "t2");
        Thread t3 = new Thread(() -> {
            service.testMethod(lock);
        }, "t3");

        t3.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(50);
        t1.start();
        Thread.sleep(1000);

        Thread notifyThread = new Thread(() -> {
            service.notifyLock(lock);
        }, "notify-thread");
        notifyThread.start();
    }

    static class Service {

        public void testMethod(Object lock) {
            String threadName = Thread.currentThread().getName();
            try {
                synchronized (lock) {
                    System.out.printf("begin wait() threadName=%s...\n", threadName);
                    lock.wait();
                    System.out.printf("end   wait() threadName=%s.\n", threadName);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void notifyLock(Object lock) {
            synchronized (lock) {
                lock.notify(); // 从 阻塞线程队列poll取 1个唤醒 ------> t3 线程 被唤醒
                lock.notify(); // 从 阻塞线程队列poll取 1个唤醒 ------> t2 线程被唤醒
                // lock.notify();
                // lock.notifyAll(); // 将阻塞线程队列的全部线程唤醒（竞争锁）
            }
        }
    }
}
