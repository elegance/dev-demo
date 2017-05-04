import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockMethodInterruptiblyTest {
    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();

        Thread threadA = new Thread(() -> service.waitMethod(), "thread-A");
        threadA.start();
        sleep(500);

        Thread threadB = new Thread(() -> service.waitMethod(), "thread-B");
        threadB.start();

//         threadA.join(); 如果在 threadB 拿到 lock后，触发 interrupt，则不会中断，如果没有拿到锁，再去 lock.lockInterruptibly() 则会抛出异常
        threadB.interrupt(); // 打标记
        System.out.println("main end!");
    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();

        public Condition condition = lock.newCondition();

        public void waitMethod() {
            String threadName = Thread.currentThread().getName();
            try {
                // lock.lock();
                lock.lockInterruptibly();
                System.out.println("lock begin " + threadName);
                for (int i = 0; i < Integer.MAX_VALUE / 10; i++) {
                    String newString = new String();
                    Math.random();
                }
                System.out.println("lock   end " + threadName);
            } catch (InterruptedException e) {
                System.out.println("线程" + threadName + "进入catch.");
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) { // 当前线程在持有锁的情况下，才执行 unlock，像线程B出现异常，没有拿到锁，就不能执行unlock操作了
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
