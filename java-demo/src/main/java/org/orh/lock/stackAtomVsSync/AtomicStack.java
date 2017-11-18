package org.orh.lock.stackAtomVsSync;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicStack<E> {

    AtomicReference<Node<E>> head = new AtomicReference<AtomicStack.Node<E>>();

    public void push(E item) {
        Node<E> oldHead;
        Node<E> newHead = new Node<E>(item);
        do {
            oldHead = head.get();
            newHead.next = oldHead;
        } while (!head.compareAndSet(oldHead, newHead));
    }

    public E pop() {
        Node<E> oldHead;
        do {
            oldHead = head.get();
            if (oldHead == null) {
                return null;
            }
        } while (!head.compareAndSet(oldHead, oldHead.next));

        return oldHead.item;
    }

    static class Node<E> {
        final E item;
        Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        AtomicStack<String> strStack = new AtomicStack<String>();
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
