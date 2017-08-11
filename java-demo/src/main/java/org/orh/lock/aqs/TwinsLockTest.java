package org.orh.lock.aqs;

import java.util.concurrent.locks.Lock;

public class TwinsLockTest {

    public static void main(String[] args) {
        final Lock lock = new TwinsLock();
        

        class Worker extends Thread {
            @Override
            public void run() {
                while (true) {
                    lock.lock();

                    try {
                        TwinsLockTest.sleep(1000L);
                        System.out.println(Thread.currentThread());
                        TwinsLockTest.sleep(1000L);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }


        for (int i = 0; i < 10; i++) {
            Worker w = new Worker();
            w.start();
        }
        
        new Thread(() -> {
            while (true) {
                TwinsLockTest.sleep(200L);
                System.out.println();
            }
        }).start();
        sleep(20000L);
    }

    public static void sleep(long x) {
        try {
            Thread.sleep(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
