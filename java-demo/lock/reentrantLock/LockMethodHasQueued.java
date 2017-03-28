import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockMethodHasQueued {
    public static void main(String[] args) {
        Service service = new Service();

        Thread threadA = new Thread(() -> {
            service.waitMethod();
        }, "thread-A");
        threadA.start();
        sleep(500);

        Thread threadB = new Thread(() -> {
            service.waitMethod();
        }, "thread-B");
        threadB.start();

        sleep(500);
        System.out.println(service.lock.hasQueuedThread(threadA)); // 判断线程 threadA 是否在等待锁 lock
        System.out.println(service.lock.hasQueuedThread(threadB)); // 判断线程 threadB 是否在等待锁 lock
        System.out.println(service.lock.hasQueuedThreads()); // 判断锁 lock 是否线程在等待获取
    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();

        public Condition condition = lock.newCondition();

        public void waitMethod() {
            try {
                lock.lock();
                sleep(Long.MAX_VALUE);
            } finally {
                lock.unlock();
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
