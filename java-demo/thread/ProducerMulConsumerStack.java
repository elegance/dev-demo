import java.util.ArrayList;
import java.util.List;

public class ProducerMulConsumerStack {

    public static void main(String[] args) {
        MyStack<String> stack = new MyStack<String>();

        Thread pThread = new Thread(() -> {
            while (true) {
                stack.push("push-" + Math.random());
            }
        }, "producer-thread");
        Thread cThread1 = new Thread(() -> {
            while (true) {
                System.out.println(stack.pop());
            }
        }, "consumer1-thread");
        Thread cThread2 = new Thread(() -> {
            while (true) {
                System.out.println(stack.pop());
            }
        }, "consumer2-thread");
        Thread cThread3 = new Thread(() -> {
            while (true) {
                System.out.println(stack.pop());
            }
        }, "consumer3-thread");
        Thread cThread4 = new Thread(() -> {
            while (true) {
                System.out.println(stack.pop());
            }
        }, "consumer4-thread");
        Thread cThread5 = new Thread(() -> {
            while (true) {
                System.out.println(stack.pop());
            }
        }, "consumer5-thread");

        pThread.start();
        cThread1.start();
        cThread2.start();
        cThread3.start();
        cThread4.start();
        cThread5.start();
    }

    static class MyStack<V> {

        private List<V> list = new ArrayList<V>();

        synchronized public void push(V v) {
            try {
                while (list.size() == 1) {
                    this.wait();
                }
                list.add(v);
                this.notifyAll();
                System.out.println("push=" + list.size());
            } catch (Exception e) {
            }
        }

        synchronized public V pop() {
            V rs = null;
            try {
                while (list.size() == 0) {
                    System.out.println("pop 操作中的：" + Thread.currentThread().getName() + " 线程呈wait状态");
                    this.wait();
                }
                rs = list.get(0);
                list.remove(0);
                this.notifyAll();
                System.out.println("pop=" + list.size());
            } catch (Exception e) {
            }
            return rs;
        }
    }
}
