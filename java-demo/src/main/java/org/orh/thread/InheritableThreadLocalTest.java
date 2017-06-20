package org.orh.thread;
import java.util.Date;

public class InheritableThreadLocalTest {
    public static InheritableThreadLocalExt t1 = new InheritableThreadLocalExt();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            print(t1.get());
            sleep(100);
        }

        sleep(2000);

        Thread a = new Thread(() -> {
            sleep(200);
            for (int i = 0; i < 10; i++) {
                print(t1.get());
                sleep(100);
            }
        }, "thread-a");
        a.start();

        t1.set("main-set-new"); // 主线程对 InheritableThreadLocal中的值修改，子线程取的值还是旧值，start之前设置的值
    }

    static class InheritableThreadLocalExt extends InheritableThreadLocal<String> {
        @Override
        protected String initialValue() {
            return new Date().getTime() + "";
        }

        @Override
        protected String childValue(String parentValue) {
            return parentValue + "-我是子线程加的~!";
        }
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
