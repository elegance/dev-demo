public class SigletonTest3 {

    public static void main(String[] args) {
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-syncMethod-1").start();
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-syncMethod-2").start();
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-syncMethod-3").start();
    }

    static class MyObject {
        // 内部类方式
        private static class MyObjectHandler {
            private static MyObject myObject = new MyObject();
        }

        private MyObject() {
            System.out.println("构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(2000);
            System.out.println("构造函数执行结束");
        }

        public static MyObject getInstance() {
            return MyObjectHandler.myObject;
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
