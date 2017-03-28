import java.util.concurrent.locks.ReentrantLock;

public class FairNoFairTest {

    public static void main(String[] args) throws InterruptedException {
        RunFair runFairThread = new RunFair();
        runFairThread.start();
        
        Thread.sleep(1000);
        
        RunNotFair runNotFairThread = new RunNotFair();
        runNotFairThread.start();
    }

    static class RunNotFair extends Thread {
        final Service service = new Service(false);

        public void run() {
            Thread[] threadArr = new Thread[10];
            for (int i = 0; i < 10; i++) {
                threadArr[i] = new Thread(() -> {
                    System.out.println("☆ 线程 " + Thread.currentThread().getName() + " 运行了");
                    service.serviceMethod();
                }, "NotFair-thread-" + i);
            }
            for (int i = 0; i < 10; i++) {
                threadArr[i].start();
            }
        }
    }
    static class RunFair extends Thread {
        final Service service = new Service(true);

        public void run() {
            Thread[] threadArr = new Thread[10];
            for (int i = 0; i < 10; i++) {
                threadArr[i] = new Thread(() -> {
                    System.out.println("★ 线程 " + Thread.currentThread().getName() + " 运行了");
                    service.serviceMethod();
                }, "fair-thread-" + i);
            }
            for (int i = 0; i < 10; i++) {
                threadArr[i].start();
            }
        }
    }

    static class Service {
        private ReentrantLock lock;

        public Service(boolean isFair) {
            lock = new ReentrantLock(isFair);
        }

        public void serviceMethod() {
            try {
                lock.lock();
                System.out.println("ThreadName=" + Thread.currentThread().getName() + " 获得锁");
            } finally {
                lock.unlock();
            }
        }
    }
}
