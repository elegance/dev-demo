package org.orh.thread;
import java.util.ArrayList;
import java.util.List;

public class ProducerConsumerStack {

    public static void main(String[] args) {
        MyStack<String> stack = new MyStack<String>();

        Thread pThread = new Thread(() -> {
            while (true) {
                stack.push("push-" + Math.random());
            }
        }, "producer-thread");
        Thread cThread = new Thread(() -> {
            while (true) {
                System.out.println(stack.pop());
            }
        }, "consumer-thread");
        
        pThread.start();
        cThread.start();
    }

    static class MyStack<V> {

        private List<V> list = new ArrayList<V>();

        synchronized public void push(V v) {
            try {
                if (list.size() == 1) {
                    this.wait();
                }
                list.add(v);
                this.notify();
                System.out.println("push=" + list.size());
            } catch (Exception e) {
            }
        }

        synchronized public V pop() {
            V rs = null;
            try {
                if (list.size() == 0) {
                    System.out.println("pop 操作中的：" + Thread.currentThread().getName() + " 线程呈wait状态");
                    this.wait();
                }
                rs = list.get(0);
                list.remove(0);
                this.notify();
                System.out.println("pop=" + list.size());
            } catch (Exception e) {
            }
            return rs;
        }
    }
}
