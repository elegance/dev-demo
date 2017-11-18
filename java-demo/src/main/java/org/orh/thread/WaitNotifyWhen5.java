package org.orh.thread;
import java.util.ArrayList;
import java.util.List;

public class WaitNotifyWhen5 {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Service service = new Service();

        Thread threadA = new Thread(() -> {
            try {
                synchronized (lock) {
                    if (service.size() != 5) {
                        System.out.println("size != 5，进入wait 状态 ， wait begin=" + System.currentTimeMillis());
                        lock.wait();
                        System.out.println("==5 了，结束wait状态，线程要退出了, wait end=" + System.currentTimeMillis());
                        throw new InterruptedException();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "thread-a");

        Thread threadB = new Thread(() -> {
            try {
                synchronized (lock) {
                    for (int i = 0; i < 10; i++) {
                        service.add();
                        if (service.size() == 5) {
                            lock.notify();
                            System.out.println("已发出通知!");
                        }
                        System.out.printf("添加了 %d 个元素\n", i + 1);
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread-b");

        threadA.start();
        Thread.sleep(50);
        threadB.start();
    }

    static class Service {
        private List<String> list = new ArrayList<String>();

        public void add() {
            list.add("阿毛");
        }

        public int size() {
            return list.size();
        }
    }
}
