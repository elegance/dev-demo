/**
 * 测试  线程 “触发wait的顺序” 与  单个notify“被唤醒的顺序”一致
 */
public class WaitOrder {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Service service = new Service(lock);

        Thread[] waitThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            waitThreads[i] = new Thread(() -> service.waitMethod(), "t-" + i);
            waitThreads[i].start();
            Thread.sleep(100); // 保证前一个线程 先进入方法
        }

        Thread.sleep(5000);
        for (int i = 0; i < 10; i++) {
            service.notifyMethod();
            Thread.sleep(10);   //模拟 间歇性的 notify，可以看到输出"唤醒结果" 与 之前输出线程“开始 wait"的顺序一致
                                // 如果 不间歇，第一个线程 唤醒后拿到锁，其他线程将”竞争锁“ 执行顺序就不可控的了。
        }
    }

    static class Service {

        public Service(Object lock) {
            this.lock = lock;
        }

        private Object lock;

        public void waitMethod() {
            String threadName = Thread.currentThread().getName();
            System.out.printf("%s 进入方法\n", threadName);
            try {
                Thread.sleep(5000);
                synchronized (lock) { // 这个时候如果线程没有拿到锁，线程将进入这个对象的 “锁竞争队列”， 不需要唤醒 会自动竞争
                    System.out.printf("%s 进入同步块(得到锁)，开始 wait\n", threadName);
                    lock.wait(); // 线程进入这个对象的 “锁wait队列”，需要手动被唤醒
                    System.out.printf("%s 被唤醒，回到同步块(得到锁)，继续执行\n", threadName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void notifyMethod() {
            try {
                synchronized (lock) {
                    // notify 线程得到锁， 触发notify
                    lock.notify();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
