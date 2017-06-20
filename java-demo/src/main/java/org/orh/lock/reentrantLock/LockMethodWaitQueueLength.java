package org.orh.lock.reentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockMethodWaitQueueLength {
    public static void main(String[] args) {
        Service service = new Service();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> service.waitMethod(), "thread-" + i).start();
        }

        sleep(2000);

        service.notifyMethod();
    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();

        public Condition condition = lock.newCondition();

        public void waitMethod() {
            try {
                lock.lock();
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void notifyMethod() {
            try {
                lock.lock();
                System.out.println("有 " + lock.getWaitQueueLength(condition) + " 个线程正在等待此condition.");
                condition.signal();
            } finally {
                lock.unlock();
            }
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
