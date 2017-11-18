package org.orh.thread;
public class TestWaitNotify {

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();

        Thread threadA = new Thread(() -> {
            service.methodWait();
        }, "thread-A");

        Thread threadB = new Thread(() -> {
            service.methodNotify();
        }, "thread-B");

        threadA.start();
        Thread.sleep(3000);
        threadB.start();
    }

    static class Service {

        private Object lock = new Object();

        public void methodWait() {
            String threadName = Thread.currentThread().getName();
            synchronized (lock) {
                try {
                    System.out.println(threadName + "   开始              wait time=" + System.currentTimeMillis());
                    lock.wait();
                    System.out.println(threadName + "   结束              wait time=" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void methodNotify() {
            String threadName = Thread.currentThread().getName();
            synchronized (lock) {
                System.out.println(threadName + "   开始              notify time=" + System.currentTimeMillis());
                lock.notify(); // 如果不触发 notify，A线程将 一直wait
                System.out.println(threadName + "   结束              notify time=" + System.currentTimeMillis());
            }
        }
    }
}
