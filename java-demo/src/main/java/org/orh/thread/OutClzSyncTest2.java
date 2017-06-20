package org.orh.thread;
public class OutClzSyncTest2 {
    static class Inner1 {
        public void method1(Inner2 innerObj) {
            String threadName = Thread.currentThread().getName();
            synchronized (innerObj) {
                System.out.printf("%s 进入 Inner1 类中的 method1 方法\n", threadName);
                for (int i = 0; i <= 10; i++) {
                    System.out.printf("i=%d\n", i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
                System.out.printf("%s 离开 Inner1 类中的 method1 方法\n", threadName);
            }
        }

        public synchronized void method2() {
            String threadName = Thread.currentThread().getName();
            System.out.printf("%s 进入 Inner1 类中的 method2 方法\n", threadName);
            for (int j = 0; j < 10; j++) {
                System.out.printf("j=%d\n", j);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            System.out.printf("%s 离开 Inner1 类中的 method2 方法\n", threadName);
        }
    }
    
    static class Inner2 {
        public synchronized void method1() {
            String threadName = Thread.currentThread().getName();
            System.out.printf("%s 进入 Inner2 类中的 method1 方法\n", threadName);
            for (int k = 0; k < 10; k++) {
                System.out.printf("k=%d\n", k);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            System.out.printf("%s 离开 Inner2 类中的 method1 方法\n", threadName);
        }
    }
}
