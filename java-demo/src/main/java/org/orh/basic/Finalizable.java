package org.orh.basic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 先手下这枚 Finalizer 的程序，reference to: http://it.deepinmind.com/gc/2014/05/13/debugging-to-understand-finalizer.html
 * 
 * TODO: 但是此处使用：JDK1.8 u45 最终没有出现 outOfMemory，系统最后是 “block”住，最终还未能分析清楚
 *
 */
public class Finalizable {
    static AtomicInteger aliveCount = new AtomicInteger(0);

    Finalizable() {
        aliveCount.incrementAndGet();
    }

    @Override
    protected void finalize() throws Throwable {
        aliveCount.decrementAndGet();
    }

    public static void main(String[] args) {
        for (int i = 0;; i++) {
            Finalizable f = new Finalizable();
            if (i % 100000 == 0) {
                System.out.format("After creating %d objects, %d are still alive.%n", new Object[] {i, Finalizable.aliveCount.get()});
            }
        }
    }

}
