import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockMethodTryLock {
    public static void main(String[] args) {
        Service service = new Service();

        new Thread(() -> service.waitMethod(), "thread-A-tryLock").start();
        new Thread(() -> service.waitMethod(), "thread-B-tryLock").start();
        new Thread(() -> service.waitMethod2(), "thread-C-tryLock(50, TimeUnit.MILLISECONDS)").start();
    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();

        public void waitMethod() {
            String threadName = Thread.currentThread().getName();
            try {
                if (lock.tryLock()) { // 如未取得锁 则退出
                    System.out.println(threadName + " 获得锁");
                    sleep(10);
                } else {
                    System.out.println(threadName + " 没有获得锁");
                }
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }

        public void waitMethod2() {
            String threadName = Thread.currentThread().getName();
            try {
                if (lock.tryLock(50, TimeUnit.MILLISECONDS)) { // 50 millis 如未取得锁则退出
                    System.out.println(threadName + " 获得锁");
                    sleep(10);
                } else {
                    System.out.println(threadName + " 没有获得锁");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
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
