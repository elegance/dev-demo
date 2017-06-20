package org.orh.thread;
public class TwoStopMultiLockObj {

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();

        Thread threadA = new Thread(() -> {
            service.methodA();
        }, "thread-A");

        Thread threadB = new Thread(() -> {
            service.methodB();
        }, "thread-B");

        threadA.start(); // methodA 一直在循环，其持有的是 相关“业务A”的锁
        threadB.start(); // methodB “业务B”无线程争抢，直接获取锁执行
        
        while(true) {
            System.out.printf("%s: %s\n", threadA.getName(), threadA.getState());
            System.out.printf("%s: %s\n", threadB.getName(), threadB.getState());
            Thread.sleep(1000);
        }
    }

    static class Service {
        private Object bizALockObj = new Object();

        private Object bizBLockObj = new Object();
        
        public void methodA() {
            synchronized(bizALockObj) {
                System.out.println("methodA begin");
                boolean isContinueRun = true; //方法内部变量，外部无法改变，这里只是为了演示本节所要表达的内容
                while (isContinueRun) {
                }
                System.out.println("methodA end");
            }
        }
        
        public void methodB() {
            synchronized(bizBLockObj) {
                System.out.println("methodB begin");
                System.out.println("methodB end");
            }
        }
    }
}
