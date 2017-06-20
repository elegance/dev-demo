package org.orh.thread;
public class WaitHasParamMethod {
    public static void main(String[] args) {
        Object lock = new Object();
        Service service = new Service();
        
        Thread t1 = new Thread(() -> {
            service.method1(lock);
        }, "thread-1");
        t1.start();
    }

    static class Service {
        public void method1(Object lock) {
            try {
                synchronized (lock) {
                    System.out.println("wait begin time=" + System.currentTimeMillis());
                    lock.wait(3000);
                    System.out.println("无人唤醒我，我将自动苏醒。");
                    System.out.println("wait   end time=" + System.currentTimeMillis());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
