package exception;

public class ThreadGroupInnerException {
    public static void main(String[] args) {
        MyThreadGroup group = new MyThreadGroup("myGroup");
        MyThread[] myThreads = new MyThread[10];
        for (int i = 0; i < myThreads.length; i++) {
            myThreads[i] = new MyThread(group, "thread-" + i, "1");
            myThreads[i].start();
        }
        MyThread newT = new MyThread(group, "thread-trigger-error", "a");
        newT.start();
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
            this.interrupt();
        }
    }
    
    static class MyThread extends Thread {
        private String num;

        public MyThread(ThreadGroup group, String threadName, String num) {
            super(group, threadName);
            this.num = num;
        }
        @Override
        public void run() {
            int numInt = Integer.parseInt(num);
            while(this.isInterrupted() == false) {
                System.out.println("死循环中：" + Thread.currentThread().getName());
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
