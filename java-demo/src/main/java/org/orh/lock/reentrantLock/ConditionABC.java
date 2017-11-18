package org.orh.lock.reentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionABC {

    volatile private static int nextPrintWho = 1;

    private static ReentrantLock lock = new ReentrantLock();

    private static Condition conditionA = lock.newCondition();
    private static Condition conditionB = lock.newCondition();
    private static Condition conditionC = lock.newCondition();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            try {
                lock.lock();
                while (nextPrintWho != 1) {
                    conditionA.await();
                }
                for (int i = 0; i < 3; i++) {
                    System.out.println("ThreadA " + (i + 1));
                }
                nextPrintWho = 2;
                conditionB.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        });
        Thread threadB = new Thread(() -> {
            try {
                lock.lock();
                while (nextPrintWho != 2) {
                    conditionB.await();
                }
                for (int i = 0; i < 3; i++) {
                    System.out.println("ThreadB " + (i + 1));
                }
                nextPrintWho = 3;
                conditionC.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        });
        Thread threadC = new Thread(() -> {
            try {
                lock.lock();
                while (nextPrintWho != 3) {
                    conditionC.await();
                }
                for (int i = 0; i < 3; i++) {
                    System.out.println("ThreadC " + (i + 1));
                }
                nextPrintWho = 1;
                conditionA.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        });

        Thread[] aArr = new Thread[5];
        Thread[] bArr = new Thread[5];
        Thread[] cArr = new Thread[5];

        for (int i = 0; i < 5; i++) {
            aArr[i] = new Thread(threadA);
            bArr[i] = new Thread(threadB);
            cArr[i] = new Thread(threadC);
            aArr[i].start();
            bArr[i].start();
            cArr[i].start();
        }

    }
}
