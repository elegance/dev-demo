package org.orh.data.structure;

import java.util.Arrays;

/**
 * 顺序存储的循环队列
 */
public class MyArrayQueue<E> {
    public static void main(String[] args) {
        MyArrayQueue<String> queue = new MyArrayQueue<>();
        System.out.println(queue.getQueueLength());

        int i;
        for (i = 1; i <= 10; i++) {
            System.out.println(queue.offer("ele-" + i));
        }
        queue.printQueue();
        System.out.println(queue.peek());
        System.out.println(queue.poll());
        queue.printQueue();
        System.out.println(queue.offer("ele-" + i));
        queue.printQueue();
        System.out.println(queue.getQueueLength());
        try {
            queue.add("ele-" + (++i));
        } catch(Exception e) {
            e.printStackTrace();
        }
        queue.poll();
        queue.add("ele-" + (++i));
        queue.printQueue();
    }

    public void printQueue() {
        System.out.printf("data: %s, front: %d, rear: %d\n", Arrays.toString(elementData), front, rear);
    }

    private Object[] elementData;

    private int front; // 队头位置

    private int rear; // 队尾位置

    public MyArrayQueue(int len) {
        elementData = new Object[len + 1];// 内部保留一个空间
    }

    public MyArrayQueue() {
        this(10);
    }

    // 获取队列元素个数
    public int getQueueLength() {
        return (rear - front + elementData.length) % elementData.length;
    }

    public boolean isEmpty() {
        return getQueueLength() == 0;
    }

    // 入队列，add 增加元素至队列尾部， 队列满时则会直接抛出异常
    public boolean add(E element) {
        if (elementData.length - getQueueLength() <= 1) {
            throw new RuntimeException("队列已满");
        }
        elementData[rear] = element;
        rear = (rear + 1) % elementData.length;

        return true;
    }

    // 入队列，offer 增加元素至队列尾部，队列满则返回false
    public boolean offer(E element) {
        if (elementData.length - getQueueLength() <= 1) {
            return false;
        }
        elementData[rear] = element;
        rear = (rear + 1) % elementData.length;

        return true;
    }

    // 取回元素，返回列头元素并删除，为空时则抛出异常
    public E remove() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空");
        }
        E res = (E) elementData[front];
        elementData[front] = null;
        front = (front + 1) % elementData.length;
        return res;
    }

    // 取回元素，返回列头元素并删除，为空时返回null
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E res = (E) elementData[front];
        elementData[front] = null;
        front = (front + 1) % elementData.length;
        return res;
    }

    public E element() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空");
        }
        return (E) elementData[front];
    }

    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return (E) elementData[front];
    }
}
