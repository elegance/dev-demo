package org.orh.sigleton;
public class SigletonTest7 {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println(MyObject.getConn().hashCode());
            }, "thread-" + i).start();
        }
    }

    static class MyObject {

        public enum MyEnumSigleton {
            connectionFactor;

            private Object conn;

            private MyEnumSigleton() {
                System.out.println("Enum 构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
                sleep(2000);
                conn = new Object();
                System.out.println("构造函数执行结束");
            }

            public Object getConn() {
                return conn;
            }
        }

        public static Object getConn() {
            return MyEnumSigleton.connectionFactor.getConn();
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
