package org.orh.thread;
public class SynchronizedMethodLockObject {

    public static void main(String[] args) {
        MyObject object = new MyObject();

        Thread threadA = new Thread(() -> {
            object.methodA(); // 进入 A synchronized 方法，持有object的锁
        }, "threadA");

        Thread threadB = new Thread(() -> {
            object.methodB(); // B 方法为非 synchronized 方法，可直接访问
            object.methodA(); // A 为 synchronized 方法，挂起等待
        }, "threadB");

        threadA.start();
        threadB.start();
    }

    static class MyObject {
        synchronized public void methodA() {
            System.out.printf("begin methodA threadName=%s\n", Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println("end");
        }

        public void methodB() {
            System.out.printf("begin methodB threadName=%s\n", Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println("end");
        }
    }
}
