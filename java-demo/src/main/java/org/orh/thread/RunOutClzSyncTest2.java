package org.orh.thread;

import org.orh.thread.OutClzSyncTest2.Inner1;
import org.orh.thread.OutClzSyncTest2.Inner2;

public class RunOutClzSyncTest2 {
    public static void main(String[] args) {
        Inner1 in1 = new Inner1();
        Inner2 in2 = new Inner2();
        Thread threadA1 = new Thread(() -> {
            in1.method1(in2);
        }, "thread-A1");

        Thread threadA2 = new Thread(() -> {
            in1.method2();
        }, "thread-A2");

        Thread threadB = new Thread(() -> {
            in2.method1();
        }, "thread-B");

        threadA1.start();
        threadA2.start();
        threadB.start();
    }
}
