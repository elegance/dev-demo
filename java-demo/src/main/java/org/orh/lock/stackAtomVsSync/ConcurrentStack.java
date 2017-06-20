package org.orh.lock.stackAtomVsSync;

public class ConcurrentStack<E> {
    Node<E> head = null;

    public synchronized void push(E item) {
        Node<E> newHead = new Node<E>(item);
        newHead.next = head;

        head = newHead;
    }

    public synchronized E pop() {
        if (head == null || head.item == null) {
            return null;
        }
        E headItem = head.item;
        head = head.next;

        return headItem;

    }

    static class Node<E> {
        E item;
        Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentStack<String> strStack = new ConcurrentStack<String>();
        int len = 204800;
        long start = System.currentTimeMillis();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < len; i++) {
                strStack.push("----" + i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < len; i++) {
                strStack.push("--------" + i);
            }
        });
        
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < len; i++) {
                strStack.push("--------" + i);
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
        
        long pushTotalTime = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();

        String rs = null;
        int cnt = 0;
        do {
            rs = strStack.pop();
            if (rs == null) {
                System.out.println("stack cnt:" + cnt);
            } else {
                cnt++;
//                System.out.println(rs);
            }

        } while (rs != null);
        System.out.println("push total time:" + pushTotalTime);
        System.out.println(" pop total time:" + (System.currentTimeMillis() - start));
    }
}
