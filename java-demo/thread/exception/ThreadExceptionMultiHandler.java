package exception;

import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadExceptionMultiHandler {
    public static void main(String[] args) {
        MyThreadGroup group = new MyThreadGroup("myGroup");
//        MyThread myThread = new MyThread();
        MyThread myThread = new MyThread(group, "myThread"); // 组异常处理 与 Thread静态的异常处理可以共存
        MyThread.setDefaultUncaughtExceptionHandler(new StateUncaughtException());
//        myThread.setUncaughtExceptionHandler(new ObjectUncaughtException()); //线程实例的  setUncaughtExceptionHandler 处理了，就轮不到别个handler机会
        myThread.start();
    }

    static class MyThreadGroup extends ThreadGroup {
        public MyThreadGroup(String name) {
            super(name);
        }

        /**
         * 重写 uncaughtException
         */
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            super.uncaughtException(t, e);
            System.out.println("线程组的异常处理");
            e.printStackTrace();
        }
    }
    static class ObjectUncaughtException implements UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("对象的异常处理");
            e.printStackTrace();
        }
    }

    static class StateUncaughtException implements UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("静态的异常处理");
            e.printStackTrace();
        }
    }

    static class MyThread extends Thread {
        private String num = "a";

        public MyThread() {
            super();
        }

        public MyThread(ThreadGroup group, String threadName) {
            super(group, threadName);
        }

        @Override
        public void run() {
            int numInt = Integer.parseInt(num);
            System.out.println("线程中打印：" + (numInt + 1));
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
