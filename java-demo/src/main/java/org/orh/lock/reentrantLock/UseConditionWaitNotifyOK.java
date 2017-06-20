package org.orh.lock.reentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UseConditionWaitNotifyOK {

    public static void main(String[] args) {
        Service service = new Service();
        new Thread(() -> service.await(), "thread-a").start();
        sleep(3000);
        service.signal(); // 主线程发出 信号
    }

    static class Service {
        private Lock lock = new ReentrantLock();

        public Condition condition = lock.newCondition();

        public void await() {
            try {
                lock.lock();
                System.out.println("await 时间为 " + System.currentTimeMillis());
                condition.await(); // 线程 WAITING, 等待 condition 信号
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void signal() {
            try {
                lock.lock();
                System.out.println("signal时间为 " + System.currentTimeMillis());
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
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
