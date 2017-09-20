package org.orh.thread;

/**
 * 重排序：指令的执行顺序与代码的顺序不一定一致，as-if-serial 不影响单线程执行结果，多线程下需要自己保证可见性问题 
 * 
 * 重排序包括：
 *  1. 编译器重排序
 *  2. 指令并级并行 - (处理器)
 *  3. 内存系统重排序 - (处理器)
 * 
 * 可见性问题，在java上有两种关键字可以解决
 *  1. synchronize - 取锁、释放锁 会同步工作内存与主内存，保证了代码块单线程执行，故可以忽略指令重排序问题
 *  2. volatile - 直接读取工作内存保证可见性
 */
public class ShareVariableSecurity {
    private boolean ready = false;

    private int result = 0;

    private int number = 1;

    public void write() {
        ready = true;               // 1.1
        number = 2;                 // 1.2
    }

    public void read() {
        if (ready) {                // 2.1
            result = number * 3;    // 2.2
        }
        System.out.println("result 的结果为： " + result);
    }

    public static void main(String[] args) {
        for (int i = 0; i< 100; i++) {
            ShareVariableSecurity instance = new ShareVariableSecurity();
            Thread t1 = instance.new TestThread(true); // 写线程
            Thread t2 = instance.new TestThread(false); // 读线程
            t1.start();
            t2.start();
        }
        
        // 分析：
        // 1. 上面依次启动的两个线程，jvm并不保证前面线程先于后面的线程开始执行，故执顺序可能有以下
        // 1.1 -> 2.1 -> -> 2.2 -> 1.2   => 1 * 3 = 3  [线程交叉执行]
        // 2.1 => 0
        // 1.2 -> 2.1 -> 1.1 => 0     [重排序、线程交叉执行]
        // 2.2 -> 2.1 -> 1.1 -> 1.2   [重排序、线程交叉执行]
        
        // 从结果中既可以看到 6，也可以看到 0
    }

    class TestThread extends Thread {
        private boolean flag;

        public TestThread(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            if (flag) {
                write();
            } else {
                read();
            }
        }
    }
}
