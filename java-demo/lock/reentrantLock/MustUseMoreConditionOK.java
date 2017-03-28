import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MustUseMoreConditionOK {
    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();
        new Thread(() -> service.awaitA(), "thread-A").start();
        new Thread(() -> service.awaitB(), "thread-B").start();
        Thread.sleep(3000);
        service.signalAll_A();
    }

    static class Service {
        private Lock lock = new ReentrantLock();

        public Condition conditionA = lock.newCondition();

        public Condition conditionB = lock.newCondition();

        public void awaitA() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                System.out.println("begin awaitA 时间为 " + System.currentTimeMillis() + " ThreadName=" + threadName);
                conditionA.await();
                System.out.println("  end awaitA 时间为 " + System.currentTimeMillis() + " ThreadName=" + threadName);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void awaitB() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                System.out.println("begin awaitB 时间为 " + System.currentTimeMillis() + " ThreadName=" + threadName);
                conditionB.await();
                System.out.println("  end awaitB 时间为 " + System.currentTimeMillis() + " ThreadName=" + threadName);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void signalAll_A() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                System.out.println(" signalAll_A 时间为 " + System.currentTimeMillis() + " ThreadName=" + threadName);
                conditionA.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void signalAll_B() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                System.out.println(" signalAll_B 时间为 " + System.currentTimeMillis() + " ThreadName=" + threadName);
                conditionB.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
