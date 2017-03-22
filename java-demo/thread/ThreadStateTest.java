/**
 * 线程状态测试
 */
public class ThreadStateTest {

    public static void main(String[] args) {
        // 建立一个不会释放的锁，供下面使用
        Object neverReleaseLock = new Object();
        new Thread(() -> {
            synchronized (neverReleaseLock) {
                while (true) {
                }
            }
        }, "T-TMP-ALWAYS-HOLD-LOCK").start();

        // 下面 开始
        Thread t1 = new Thread(() -> {
        }, "T-NEW-NOT-START"); // 新建了没有 start 的线程
        print(t1);

        Thread t2 = new Thread(() -> {
            while (true) {
            }
        }, "T-ALWAYS-RUNNING"); // 一直在运行的线程
        t2.start();
        print(t2);

        Thread t3 = new Thread(() -> {
            sleep(2000);
        }, "T-SLEEP(long)"); // SLEEP的线程
        t3.start();
        print(t3);

        Thread t4 = new Thread(() -> {
            synchronized (neverReleaseLock) {
            }
        }, "T-WAIT-LOCK"); // 等待拿锁的线程

        t4.start();
        print(t4);

        Object t5Lock = new Object();
        Thread t5 = new Thread(() -> {
            synchronized (t5Lock) {
                wait(t5Lock); // 为了使这里没有try-catch影响直观性，将其移入了方法 ，直接等同于 t5Lock.wait();
            }
        }, "T-WAIT()"); // 调用了 wait() 方法后的线程
        t5.start();
        print(t5);

        Object t6Lock = new Object();
        Thread t6 = new Thread(() -> {
            synchronized (t6Lock) {
                wait(t6Lock, 300); // t6Lock.wait(300);
            }
        }, "T-WAIT(long)"); // 调用了 wait(long) 方法后的线程
        t6.start();
        print(t6);

        Thread t7 = new Thread(() -> {
            Thread t7ChildTmp = new Thread(() -> { sleep(1000); });
            t7ChildTmp.start();
            join(t7ChildTmp); // 等同 t7ChildTmp.join();
        }, "T-INNER-EXCUTE-JOIN()"); // 内部调用了 join() 方法后的线程
        t7.start();
        print(t7);

        Thread t8 = new Thread(() -> {
            Thread t8ChildTmp = new Thread(() -> { sleep(1000); });
            t8ChildTmp.start();
            join(t8ChildTmp, 2000); // 等同 t8ChildTmp.join(2000);
        }, "T-INNER-EXCUTE-JOIN(long)"); // 内部调用了 join(long) 方法后的线程
        t8.start();
        print(t8);

        Thread t9 = new Thread(() -> {}, "T-FINISHD");
        t9.start();
        print(t9);

        System.exit(0);
        
        // 总结：
        
        // 被动状态（意味着无限等待，多多注意，重点关注）：
        // 1. BLOCKED : 依赖其他线程释放锁对象，主要在使用 synchronized块、synchronized方法 出现 
        // 2. WATING  ：依赖其他线程唤醒或执行完，主要在使用 wait() 、join() 时出现，wait()时需要线程接收到所对象的notify()/notifyAll()，join()则需要等待线程执行完
        
        // 较为安全的状态
        // 1. NEW ：新建了线程却未启动它
        // 2. RUNNABLE: 有可能正在运行(RUNNING)，也有可能在就绪队列中(Ready)，这个取决于 线程调度器
        // 3. TIMED_WAITING: 挂起，等待指定的时间后自动恢复
        // 4. TERMINATED: 已经执行完毕了的线程
    }

    public static void print(Thread thread) {
        sleep(100);
        System.out.printf("%-25s: state=%-15s, isAlvie=%s\n", thread.getName(), thread.getState(), thread.isAlive());
    }

    // 一下都是 一些辅助 上面直观表达的帮助函数，避免 lambda 使用try-catch
    private static void join(Thread thread, long millis) {
        try {
            thread.join(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait(Object lockObj) {
        try {
            lockObj.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait(Object lockObj, long millis) {
        try {
            lockObj.wait(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
