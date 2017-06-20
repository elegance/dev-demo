package org.orh.thread;
public class VerifyIsolation {
    static class ThreadLocalExt extends ThreadLocal<String> {
        @Override
        protected String initialValue() { //重写 
            return "未设值时的默认值";
        }
    }

    public static ThreadLocalExt t1 = new ThreadLocalExt();

    public static void main(String[] args) {
        print(t1.get());
        
        Thread a = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                t1.set("thread-a" + (i + 1));
                print(t1.get());
                sleep(200);
            }
        }, "thread-a");

        Thread b = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                t1.set("thread-b" + (i + 1));
                print(t1.get());
                sleep(200);
            }
        }, "thread-b");
        a.start();
        b.start();

        for (int i = 0; i < 100; i++) {
            t1.set("thread-Main" + (i + 1));
            print(t1.get());
            sleep(200);
        }
        print(t1.get());
    }

    public static void print(String value) {
        String threadName = Thread.currentThread().getName();
        System.out.printf("%-15s get Value=%s\n", threadName, value);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
