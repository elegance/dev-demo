package org.orh.thread;
public class ChangeLockString {

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();

        Thread threadA = new Thread(() -> {
            service.testMethod();
        }, "thread-A");

        Thread threadB = new Thread(() -> {
            service.testMethod();
        }, "thread-B");
        
        threadA.start();
        Thread.sleep(50);
        threadB.start();
        
        
        Thread threadC = new Thread(() -> {
            service.testMethod();
        }, "thread-C");

        Thread threadD = new Thread(() -> {
            service.testMethod();
        }, "thread-D");
        threadC.start();
        Thread.sleep(50);
        threadD.start();
    }

    static class Service {
        private String lock = "123";

        public void testMethod() {
            String threadName = Thread.currentThread().getName();
            try {
                synchronized (lock) {
                    System.out.printf("%s begin %d\n", threadName, System.currentTimeMillis());
                    lock = "456"; // 改变之
                    Thread.sleep(2000);
                    System.out.printf("%s end %d\n", threadName, System.currentTimeMillis());
                }
            } catch (Exception e) {

            }
        }

        private LockObj lockObj = new LockObj();
        
        class LockObj {
            String value = "hello";
        }
        public void testMethod2() {
            String threadName = Thread.currentThread().getName();
            try {
                synchronized (lockObj) {
                    System.out.printf("%s begin %d\n", threadName, System.currentTimeMillis());
                    lockObj.value = "world"; // 改变对象中的值
                    Thread.sleep(2000);
                    System.out.printf("%s end %d\n", threadName, System.currentTimeMillis());
                }
            } catch (Exception e) {

            }
        }
    }
}
