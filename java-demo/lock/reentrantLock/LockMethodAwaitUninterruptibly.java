import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockMethodAwaitUninterruptibly {
    public static void main(String[] args) {
        Service service = new Service();

        Thread threadA = new Thread(() -> service.testMethod(), "thread-A");
        threadA.start();
        sleep(3000);
        threadA.interrupt();
    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();
        public Condition condition = lock.newCondition();

        public void testMethod() {
            try {
                lock.lock();
                System.out.println("wait begin");
                // condition.await(); // 这种方式，线程将被打断，正常抛出异常
                condition.awaitUninterruptibly(); // main中触发 interrupt 后，这里不会产生异常，继续执行
                System.out.println("wait end");
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
