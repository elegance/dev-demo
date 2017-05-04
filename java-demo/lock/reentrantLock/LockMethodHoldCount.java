import java.util.concurrent.locks.ReentrantLock;

public class LockMethodHoldCount {
    public static void main(String[] args) {
        Service service = new Service();

        new Thread(() -> {
            service.serviceMethod1();
        }, "thread-a").start();
        
        sleep(500);

        System.out.println("mainThread getHoldCount=" + service.lock.getHoldCount());

    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();

        public void serviceMethod1() {
            try {
                lock.lock();
                System.out.println("serviceMethod1 getHoldCount=" + lock.getHoldCount());
                sleep(1000);
                serviceMethod2();
            } finally {
                lock.unlock();
            }
        }

        public void serviceMethod2() {
            try {
                lock.lock();
                System.out.println("serviceMethod2 getHoldCount=" + lock.getHoldCount());
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
