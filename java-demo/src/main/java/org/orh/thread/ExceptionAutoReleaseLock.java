package org.orh.thread;
public class ExceptionAutoReleaseLock {

    public static void main(String[] args) {
        Service service = new Service();

        Thread a = new Thread(() -> {
            service.testMethod();
        }, "a");

        Thread b = new Thread(() -> {
            service.testMethod();
        }, "b");
        
        a.start();
        b.start();
    }

    static class Service {
        synchronized public void testMethod() {
            if ("a".equals(Thread.currentThread().getName())) {
                System.out.printf("ThradName=%s run beginTime=%d\n", Thread.currentThread().getName(), System.currentTimeMillis());
                int i = 1;
                while (i == 1) {
                    if (("" + Math.random()).substring(0, 8).equals("0.123456")) {
                        System.out.printf("ThradName=%s run exceptionTime=%d\n", Thread.currentThread().getName(), System.currentTimeMillis());
                        Integer.parseInt("a");
                    }
                }
            } else {
                System.out.printf("ThradName=%s run time=%d\n", Thread.currentThread().getName(), System.currentTimeMillis());
            }
        }
    }
}
