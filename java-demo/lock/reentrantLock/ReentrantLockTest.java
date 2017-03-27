import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        Service service = new Service();
        
        new Thread(() -> service.testMethod(), "thread-1").start();
        new Thread(() -> service.testMethod(), "thread-2").start();
        new Thread(() -> service.testMethod(), "thread-3").start();
        new Thread(() -> service.testMethod(), "thread-4").start();
        new Thread(() -> service.testMethod(), "thread-5").start();
    }

    static class Service {
        private Lock lock = new ReentrantLock();

        public void testMethod() {
            String threadName = Thread.currentThread().getName();
            lock.lock();
            for (int i = 0; i < 10; i++) {
                System.out.printf("ThreadName=%-15s (%d)\n", threadName, (i + 1));
            }
            lock.unlock();
        }
    }
}
