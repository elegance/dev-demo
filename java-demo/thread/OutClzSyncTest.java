public class OutClzSyncTest {
    static class Inner {
        public void method1() {
            String threadName = Thread.currentThread().getName();
            synchronized ("其他的锁") {
                for (int i = 0; i <= 10; i++) {
                    System.out.printf("%s i=%d\n", threadName, i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public synchronized void method2() {
            String threadName = Thread.currentThread().getName();
            for (int i = 11; i <= 20; i++) {
                System.out.printf("%s i=%d\n", threadName, i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
