package org.orh.thread;
public class DaemonTest {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            int i = 0;
            while (true) {
                System.out.printf("i=%d\n", ++i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.setDaemon(true);
        t1.start();
        Thread.sleep(5000);
        System.out.println("离开thread对象也不再打印了，也就是停止了！");
        // 理解：用户线程 main都结束了，守护线程们已经没有什么可守护的了，就结束了
    }
}
