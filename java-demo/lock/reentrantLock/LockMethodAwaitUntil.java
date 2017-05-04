import java.util.Calendar;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockMethodAwaitUntil {
    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();

        // 测试一：threadA 到指定的时间会自动唤醒
        Thread threadA = new Thread(() -> service.testMethod(), "thread-A");
        threadA.start();
        
        threadA.join(); 
        
        // 测试二: threadA1 没有到达指定的时间，提前被threadB1 notifyAll()唤醒
        Thread threadA1 = new Thread(() -> service.testMethod(), "thread-A1");
        threadA1.start();
        
        sleep(2000);
        
        Thread threadB1 = new Thread(() -> service.notifyMethod(), "thread-B1");
        threadB1.start();
    }

    static class Service {
        public ReentrantLock lock = new ReentrantLock();
        public Condition condition = lock.newCondition();

        public void testMethod() {
            try {
                lock.lock();
                // condition.await(); // 这种方式，线程将被打断，正常抛出异常
                // condition.awaitUninterruptibly(); // main中触发 interrupt 后，这里不会产生异常，继续执行
                Calendar calendarRef = Calendar.getInstance();
                calendarRef.add(Calendar.SECOND, 10);

                System.out.println("wait begin timer=" + System.currentTimeMillis());
                condition.awaitUntil(calendarRef.getTime()); // 直到这个时间点，自动唤醒
                System.out.println("wait   end timer=" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }

        public void notifyMethod() {
            try {
                Calendar calendarRef = Calendar.getInstance();
                calendarRef.add(Calendar.SECOND, 10);
                lock.lock();
                System.out.println("notify begin timer=" + System.currentTimeMillis());
                condition.signalAll();
                System.out.println("notify   end timer=" + System.currentTimeMillis());
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
