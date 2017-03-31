public class SigletonTest6 {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println(MyObject.connectionFactory.getConn().hashCode());

                System.out.println("   ignore B:" + MyObject.connectionFactoryB.getConn().hashCode());
            }, "thread-" + i).start();
        }
    }

    static enum MyObject {
        connectionFactory,  // 枚举的元素，就像静态资源，会被初始化执行，构造函数将自动执行

        connectionFactoryB; // 实际中只有上面的一个枚举元素， 多定义1个枚举只是为了便于理解 此处的枚举实例，单个枚举实例放在此处看起来手法晦涩

        private Object conn;

        // 上面定义 了两个实例，所以此处的构造函数，默认会被执行两遍【产生两个实例，请忽略B的输出】
        // 跟往常一样-看到的线程名称是 thread-[0-5]中的某个线程，而不是main，说明jvm优化 完全没有使用到的静态类，不会执行其静态初始化动作
        private MyObject() {
            System.out.println("构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(1000);
            conn = new Object();
            System.out.println("构造函数执行结束");
        }

        public Object getConn() {
            return conn;
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
