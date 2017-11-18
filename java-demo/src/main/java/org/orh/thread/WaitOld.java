package org.orh.thread;
import java.util.ArrayList;
import java.util.List;

public class WaitOld {

    public static void main(String[] args) throws InterruptedException {
        String lock = new String("");

        Service service = new Service(lock);

        Thread subtract1Thread = new Thread(() -> {
            service.subtract();
        }, "subtract1-thread");
        Thread subtract2Thread = new Thread(() -> {
            service.subtract();
        }, "subtract2-thread");

        subtract1Thread.start();
        subtract2Thread.start();
        Thread.sleep(1000);
        
        Thread addThread = new Thread(() -> {
            service.add();
        }, "add-thread");
        addThread.start();
        
    }

    static class Service {

        private String lock;

        public Service(String lock) {
            this.lock = lock;
        }

        public void add() {
            synchronized (lock) {
                ValueObject.list.add("anyString");
                lock.notifyAll();
            }
        }

        public void subtract() {
            try {
                String threadName = Thread.currentThread().getName();
                synchronized (lock) {
                    if (ValueObject.list.size() == 0) {
               //while (ValueObject.list.size() == 0) {
                        System.out.println("wait begin threadName=" + threadName);
                        lock.wait(); // 两个线程 wait 阻塞在这里，notifyAll 时，都被唤醒
                        System.out.println("wait end   threadName=" + threadName);
                    }
                    ValueObject.list.remove(0); // 两个线程依次都已经经过 size() == 0 的约束，但是add只有一次，notifyAll 提醒了全部，这里会发生数组越界
                                                // 将上面的 if 换成 while， wait 之后再判断
                    
                    System.out.println("list size=" + ValueObject.list.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class ValueObject {
        public static List<String> list = new ArrayList<String>();
    }
}
