package org.orh.lock.reentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTestManyToMany {

    public static void main(String[] args) {
        Service service = new Service();
        Thread[] threadsA = new Thread[10];
        Thread[] threadsB = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            threadsA[i] = new Thread(() -> {
                for (int j = 0; j < Integer.MAX_VALUE; j++) {
                    service.set();
                }
            }, "thread-A");

            threadsB[i] = new Thread(() -> {
                for (int j = 0; j < Integer.MAX_VALUE; j++) {
                    service.get();
                }
            }, "thread-B");

            threadsA[i].start();
            threadsB[i].start();
        }
    }

    static class Service {
        private Lock lock = new ReentrantLock();

        private Condition condition = lock.newCondition();

        private boolean hasValue = false;

        public void set() {
            try {
                lock.lock();
                while (hasValue) {
                    System.out.println("有可能★★连续");
                    condition.await(); // 有值未被消费，进入`WAITING`
                }
                System.out.println("打印★");
                hasValue = true;
//                condition.signal(); // 设值完后，发出唤醒信号，唤醒消费线程，但实际可能唤醒的是另一个生产线程，可能导致假死
                condition.signalAll(); // 故使用signallAll 唤醒等待此条件的所有线程
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
                    System.out.println("有可能☆☆连续");
                    condition.await(); // 没有值，进入 `WAITING`状态
                }
                System.out.println("打印☆");
                hasValue = false;
//                condition.signal(); // 消费完后，发出唤醒信号，唤醒生产线程，但实际可能唤醒的是另一个消费线程，可能导致假死
                condition.signalAll(); // 故使用signallAll 唤醒等待此条件的所有线程
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
