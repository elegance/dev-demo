import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockBegin2 {
    public static void main(String[] args) {
        Service service = new Service();
        new Thread(() -> service.write(), "thread-A").start();
        new Thread(() -> service.write(), "thread-B").start();
    }

    static class Service {

        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        public void write() {
            String threadName = Thread.currentThread().getName();
            try {
                lock.writeLock().lock();
                System.out.println("获得写锁 " + threadName + " " + System.currentTimeMillis());
                sleep(10 * 1000); // 持有读锁 10秒钟
            } finally {
                lock.writeLock().unlock();
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
