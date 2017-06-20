package org.orh.thread;
public class SuspendResumeDealLock {

    static class SynchronizedObject {
        synchronized public void printString() {
            System.out.println("begin");

            if (Thread.currentThread().getName().equals("a")) {
                System.out.println("a 线程永远suspend了");
                Thread.currentThread().suspend();
            }
            System.out.println("end");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final SynchronizedObject object = new SynchronizedObject();

        Thread thread1 = new Thread(() -> {
            object.printString();
        });
        thread1.setName("a");
        thread1.start();
        Thread.sleep(1000);

        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 启动了，但进入不了printString() 方法！只有 a 线程了打印了1个begin");
            System.out.println("因为pringString()方法被 a 线程锁定并且永远suspend暂停了！");
            object.printString();
        });
        thread2.start();
    }
}
