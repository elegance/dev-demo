import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MustUseMoreConditionError {
    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();
        new Thread(() -> service.awaitA(), "thread-A").start();
        new Thread(() -> service.awaitB(), "thread-B").start();
        
        Thread.sleep(3000);
        service.signalAll();
    }

    static class Service {
        private Lock lock = new ReentrantLock();

        public Condition condition = lock.newCondition();

        public void awaitA() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                System.out.printf("begin awaitA 时间为 %d threadName=%s\n", System.currentTimeMillis(), threadName);
                condition.await();
                System.out.printf("  end awaitA 时间为 %d threadName=%s\n", System.currentTimeMillis(), threadName);
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }
        }

        public void awaitB() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                System.out.printf("begin awaitB 时间为 %d threadName=%s\n", System.currentTimeMillis(), threadName);
                condition.await();
                System.out.printf("  end awaitB 时间为 %d threadName=%s\n", System.currentTimeMillis(), threadName);
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }
        }

        public void signalAll() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                System.out.printf(" signalAll 时间为 %d threadName=%s\n", System.currentTimeMillis(), threadName);
                condition.signalAll();
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }
        }
    }
}
