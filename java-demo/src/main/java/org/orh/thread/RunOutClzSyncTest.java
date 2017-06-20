package org.orh.thread;

import org.orh.thread.OutClzSyncTest.Inner;

public class RunOutClzSyncTest {
    public static void main(String[] args) {
        Inner inner = new Inner();
        Thread threadA = new Thread(() -> {
            inner.method1();
        }, "thread-A");

        Thread threadB = new Thread(() -> {
            inner.method2();
        }, "thread-B");
        
        threadA.start();
        threadB.start();
    }
}
