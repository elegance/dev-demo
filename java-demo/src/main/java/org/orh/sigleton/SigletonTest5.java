package org.orh.sigleton;
import java.io.IOException;

public class SigletonTest5 {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-1").start();
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-2").start();
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-3").start();
    }

    static class MyObject {
        private static MyObject myObject = null;

        private MyObject() {
            System.out.println("构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(2000);
            System.out.println("构造函数执行结束");
        }

        static {
            myObject = new MyObject();
        }

        public static MyObject getInstance() {
            return myObject;
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
