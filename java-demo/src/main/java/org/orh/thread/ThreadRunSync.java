package org.orh.thread;
public class ThreadRunSync {
    public static void main(String[] args) {
        Object lock = new Object();

        new Thread(() -> {
            new Servcie(lock, "A", 1).run();
        }, "thread-a").start();

        new Thread(() -> {
            new Servcie(lock, "B", 2).run();
        }, "thread-b").start();

        new Thread(() -> {
            new Servcie(lock, "C", 0).run();
        }, "thread-c").start();
    }

    static class Servcie {
        private Object lock;
        private String showChar;
        private int showNumPosition;
        private int printCount; // 统计打了几个字母
        volatile private static int addNumber = 1;

        public Servcie(Object lock, String showChar, int showNumPosition) {
            this.lock = lock;
            this.showChar = showChar;
            this.showNumPosition = showNumPosition;
        }

        public void run() {
            try {
                synchronized (lock) {
                    while (true) {
                        if (addNumber % 3 == showNumPosition) { // 累计打印次数 对 3 取余，如果结果等于 线程 要展示的位置，则进入，否则继续等待 ==>  1%3=1，2%3=2, 3%3=0，4%3=1, 5%3=2, 6%3=0, 7%3=1, 8%3=2, 9%3=2
                            System.out
                                    .println("ThreadName=" + Thread.currentThread().getName() + " runCount=" + addNumber + " " + showChar);
                            lock.notifyAll();
                            addNumber++;
                            printCount++;
                            if (printCount == 3) { // 单个实例只打印 3 次
                                break;
                            }
                        } else {
                            lock.wait();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
