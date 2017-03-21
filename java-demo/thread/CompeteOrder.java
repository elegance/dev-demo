public class CompeteOrder {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Service service = new Service(lock);

        new Thread(() -> service.holdLock(3000), "hold-lock-thread").start(); // 保证下面的线程一开始 都拿不到锁

        for (int i = 0; i < 100; i++) {
            new Thread(() -> service.syncMethod(), "thread-" + i).start();
            Thread.sleep(10); // 保证进入 thread 方法的顺序，也就是进入 就绪队列的顺序
        }

        // thread-99 最后进入syncMethod，确最先拿到锁，率先执行完
        // 然后依次 98，97，96....
        // thread-0 最先进入 syncMethod ，却最后拿到锁，最后执行完
        
        // 结论 ：进入就绪队列的顺序，与拿到锁的顺序相反，也就是最后进入 方法的最先执行，就类似于"栈"，后进先出
    }

    static class Service {
        private Object lock;

        public Service(Object lock) {
            this.lock = lock;
        }

        public void holdLock(long millis) {
            try {
                synchronized (lock) {
                    Thread.sleep(millis);
                }
            } catch (Exception e) {
            }
        }

        public void syncMethod() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " 进入方法");
            try {
                synchronized (lock) {
                    System.out.println(threadName + " 拿到了锁，执行完毕.");
                    Thread.sleep(100);
                }
            } catch (Exception e) {
            }
        }
    }
}
