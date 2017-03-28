import java.util.concurrent.locks.ReentrantLock;

public class LockMethodQueueLength {
    public static void main(String[] args) {
        Service service = new Service();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> service.serviceMethod1(), "thread-" + i).start();
        }

        sleep(2000);

        System.out.println("mainThread getQueueLength=" + service.lock.getQueueLength());

    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();

        public void serviceMethod1() {
            try {
                lock.lock();
                System.out.println("ThreadName=" + Thread.currentThread().getName() + " 进入方法");
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
