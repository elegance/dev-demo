package org.orh.lock.reentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    public static void main(String[] args) {
        Service service = new Service();
        new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                service.set();
            }
        }, "thread-A").start();
        new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                service.get();
            }
        }, "thread-B").start();
    }

    static class Service {
        private Lock lock = new ReentrantLock();

        private Condition condition = lock.newCondition();

        private boolean hasValue = false;

        public void set() {
            try {
                lock.lock();
                while (hasValue) {
                    condition.await(); // 有值未被消费，进入`WAITING`
                }
                System.out.println("打印★");
                hasValue = true;
                condition.signal(); // 设值完后，发出唤醒信号，唤醒消费线程
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void get() {
            try {
                lock.lock();
                while (!hasValue) {
                    condition.await(); // 没有值，进入 `WAITING`状态
                }
                System.out.println("打印☆");
                hasValue = false;
                condition.signal(); // 消费完后，发出唤醒信号，唤醒生产线程
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
