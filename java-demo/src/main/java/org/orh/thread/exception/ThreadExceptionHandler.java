package org.orh.thread.exception;

import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadExceptionHandler {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                System.out.println("begin ThreadName=" + Thread.currentThread().getName());
                String userName = null;
                System.out.println(userName.hashCode());
            }
        };
        Thread t1 = new Thread(runnable, "t-1");

        t1.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("线程：" + t.getName() + " 出现了异常：");
                // e.printStackTrace();
                System.out.println("  end ThreadName=" + t.getName());
            }
        });
        t1.start();

        Thread t2 = new Thread(runnable, "t-2");
        t2.start();

        Thread.sleep(1000);
        System.out.println("hello");
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("线程：" + t.getName() + " 出现了异常（setDefaultUncaughtExceptionHandler）");
                // e.printStackTrace();
                System.out.println("  end ThreadName=" + t.getName());
            }
        });
        new Thread(t1, "t-1-1").start(); // 你可以发现 t-1-1 木有 打印任何信息，很是神奇呀!!! ? <br>
        // 是因为t1发生异常了么？ 
        // 深入研究：Thread newThread = new Thread(oldThread); <br>
        // newThread 与 oldThread的关系：发现oldThread如果是一个执行完毕了的线程 TERMINATED ，newThread 则不会执行，但是newThread还是 会经过三个状态：NEW、RUNNABLE、TERMINATED
        new Thread(runnable, "t-thread-hasDefaultHandler").start();
    }
}
