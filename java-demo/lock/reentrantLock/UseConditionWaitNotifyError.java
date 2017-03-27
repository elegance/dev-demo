import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UseConditionWaitNotifyError {

    public static void main(String[] args) {
        Service service = new Service();

//        new Thread(() -> service.await(), "thread-a").start(); // 报：
                                                               // IllegalMonitorStateException，因为在执行
                                                               // await时需要获得锁 lock
        
        new Thread(() -> service.awawit2(), "thread-b").start();
    }

    static class Service {
        private Lock lock = new ReentrantLock();

        private Condition condition = lock.newCondition();

        public void await() {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void awawit2() {
            try {
                lock.lock();
                System.out.println("A");
                condition.await();
                System.out.println("B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("锁释放了!");
            }
        }

    }
}
