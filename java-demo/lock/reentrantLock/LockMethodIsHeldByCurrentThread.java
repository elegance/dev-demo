import java.util.concurrent.locks.ReentrantLock;

public class LockMethodIsHeldByCurrentThread {
    public static void main(String[] args) {
        Service service = new Service();
        System.out.println("  lock isFair:" + service.lock.isFair()); //默认为非公平锁

        System.out.println("lock isLocked:" + service.lock.isLocked());
        new Thread(() -> service.serviceMethod(), "thread-t").start();
        sleep(200);
        System.out.println("lock isLocked:" + service.lock.isLocked());
    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();

        public void serviceMethod() {
            try {
                System.out.println("lock isHeldByCurrentThread:" + lock.isHeldByCurrentThread());
                lock.lock();
                System.out.println("lock isHeldByCurrentThread:" + lock.isHeldByCurrentThread());
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
