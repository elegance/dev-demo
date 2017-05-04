package exception;

/**
 * <h2>研究："Thread newThread = new Thread(oldThread);" oldThread的运行状态对newThread影响 <h2>
 * <h3>结论：</h3>
 * 1. oldThread 处于NEW、RUNNABLE、BLOCKED、WAITING/TIMED_WAITING 时，newThread.start()，newThread能正常的运作执行；<br>
 *   当oldThread状态为TERMINATED时，newThread.start()，newThread 会经过NEW/RUNNABLE/TIMINATED三个状态，但是newThread内部的run方法不会被执行 <br>
 * 2.  所以要科学的使用 new Thread(existThread); 使用这种做法，应该简单的理解existThread其用意就是用来被包装执行的，不应该被start。 上面发现这种情况也是因为在测试过程中减少代码的写法发现的。
 */
public class NewThreadForThread {
    // Thread threadNew = new Thread(threadOld)
    // threadNew 与 threadOld 的关系
    // 1. threadOld 启动前、运行中、完成后 对 threadNew 有何影响？
    // 2. threadOld 的其他状态对 threadNew 有何影响？
    // 3. 结合上面的情况， threadOld 执行过程中发生异常呢？

    public static void main(String[] args) {
        testCase1();
        testCase2();

    }
    
    // 根据前两步的， 情况 测试：异常发生后且线程还未结束 ；因异常前：1、2步已证明，可正常开始新的thread，异常后TERMINATED 新的thread 不会执行内部
    // 想要在：异常发生且线程还未结束，只能是捕捉住异常，进入一段时间的 RUNNABLE/BLOCKED/WAITING/TIMED_WAITING ，在这段时间内去new新的线程
    // 其实不用测试，我们就可以推出结论了: 因为线程没有“异常”这个状态，这个属于迷惑选项，线程异常未结束无非就是属于这几个状态：RUNNABLE/BLOCKED/WAITING/TIMED_WAITING
    public static void testCase3() {
    }

    /**
     * 测试在 oldThread 处于 waiting/blocked 时，newThread的表现情况
     * 1. 都正常执行
     */
    public static void testCase2() {
        System.out.println("===============================testCase2-begin====================================");
        // 准备两把锁 ，供测试状态使用
        Object lock = new Object();
        Object neverReleaseLock = new Object();
        Thread neverReleaseLockThread = new Thread(() -> {
            synchronized (neverReleaseLock) {
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    // 正常退出，释放锁，结束 testCase2
                }
            }
        });
        neverReleaseLockThread.start();

        // 开始测试逻辑
        ThreadGroup test2Group = new ThreadGroup("testCaes2-group");

        Thread threadOldAlsBlocked = new Thread(test2Group, () -> {
            printBeginInfo();
            if (Thread.currentThread().getName().contains("threadOld")) {
                synchronized (neverReleaseLock) { // 线程进入 BLOCKED 状态
                }
            }
            printEndInfo();
        }, "threadOldAlsBlocked");

        // BLOCKED
        threadOldAlsBlocked.start();
        sleep(100); // 确保 threadOldAlsBlocked 已经开始
        Thread threadNew1 = new Thread(test2Group, threadOldAlsBlocked, "thread-with-BLOCKED");
        System.out.println("1. threadOld state:" + threadOldAlsBlocked.getState());
        threadNew1.start();

        // WAITING
        Thread threadOldAlsWaiting = new Thread(test2Group, () -> {
            printBeginInfo();
            if (Thread.currentThread().getName().contains("threadOld")) {
                synchronized (lock) {
                    try {
                        lock.wait(); // 线程进入 WAITING 状态
                    } catch (InterruptedException e) {
                    }
                }
            }
            printEndInfo();
        }, "threadOldAlsWaiting");
        threadOldAlsWaiting.start();

        sleep(100); // 确保 threadOldAlsWaiting 已经开始
        Thread threadNew2 = new Thread(test2Group, threadOldAlsBlocked, "thread-with-WAITING");
        System.out.println("2. threadOld state:" + threadOldAlsBlocked.getState());
        threadNew2.start();

        sleep(300);

        System.out.println("释放锁，解除BLOCKED");
        neverReleaseLockThread.interrupt(); // 释放锁
        sleep(100);
        synchronized (lock) {
            System.out.println("lock唤醒，解除WAITING");
            lock.notify(); // 解锁，WAITING
        }

        while (test2Group.activeCount() > 0) {
            Thread.yield();
        }
        System.out.println("===============================testCase2-end====================================");
    }

    public static void printBeginInfo() {
        System.out.println("======begin ThreadName=" + Thread.currentThread().getName());
    }

    public static void printEndInfo() {
        System.out.println("======  end ThreadName=" + Thread.currentThread().getName());
    }

    /**
     * testCase1 的执行情况是： 1. oldThread 处于 NEW/RUNNABLE时 ，newThread 都能正常执行 2. oldThread
     * 处于TERMINATED，才开始 newThread 会经历线程的 3个阶段：NEW、RUNNABLE、TERMINATED 但是 线程内的 代码却没有执行！
     */
    public static void testCase1() {
        System.out.println("===============================testCase1-begin====================================");
        ThreadGroup test1Group = new ThreadGroup("testCaes1-group");

        Thread threadOld = new Thread(test1Group, () -> {
            printBeginInfo();
            if (Thread.currentThread().getName().equals("threadOld")) {
                while (!Thread.currentThread().isInterrupted()) {
                }
            }
            printEndInfo();
        }, "threadOld");

        // 1. NEW
        Thread threadWitnNew = new Thread(test1Group, threadOld, "thread-with-NEW");
        System.out.println("1. threadOld state:" + threadOld.getState());
        threadWitnNew.start();

        // 2. RUNNABLE
        sleep(100); // 给时间给 threadNew1 执行完
        threadOld.start();
        sleep(100); // 确保 threadOld 已经开始
        Thread threadWithRunnable = new Thread(test1Group, threadOld, "thread-with-RUNNABLE");
        System.out.println("2. threadOld state:" + threadOld.getState());
        threadWithRunnable.start();

        // 3. TERMINATED
        sleep(100); // 给时间给 threadNew2 执行完
        threadOld.interrupt();
        sleep(100); // 给时间给 threadOld.interrupt() 执行响应完
        Thread threadWithTerminated = new Thread(test1Group, threadOld, "thread-with-TERMINATED");
        System.out.println("3. threadOld state:" + threadOld.getState());

        System.out.println("threadNew3 state:" + threadWithTerminated.getState());
        threadWithTerminated.start();
        System.out.println("threadNew3 state:" + threadWithTerminated.getState());

        while (test1Group.activeCount() > 0) {
            Thread.yield();
        }
        System.out.println("threadNew3 state:" + threadWithTerminated.getState());
        System.out.println("===============================testCase1-end====================================");
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
