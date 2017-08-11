package org.orh.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("Ruannable hit!"); // 每批次线程开始执行都会触发
            }
        });

        for (int i = 1 ; i <= 7; i++) {
            final int x = i;
            if (i % 3 != 0) {
                Thread.sleep(2000);
            }
            new Thread(() -> {
                try {
//                    System.out.println("cyclicBarrier watings: " + cyclicBarrier.getNumberWaiting() + " parities:" + cyclicBarrier.getParties());
                    cyclicBarrier.await(); // 等待 达到 cyclic 的数量
                    System.out.println("thread-" + x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
