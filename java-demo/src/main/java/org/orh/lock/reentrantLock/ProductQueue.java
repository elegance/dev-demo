package org.orh.lock.reentrantLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProductQueue<T> {

    private final T[] items;

    private final Lock lock = new ReentrantLock();

    private Condition notFull = lock.newCondition();

    private Condition notEmpty = lock.newCondition();

    private int head, tail, count;

    public ProductQueue(int maxSize) {
        items = (T[]) new Object[maxSize];
    }

    public ProductQueue() {
        this(10);
    }

    public void put(T t) throws InterruptedException {
        lock.lock();

        try {
            while (count == getCapacity()) { // put 放：队列满了时，则挂起(await)，直到收到notFull的信号
                System.out.println("put wait...");
                notFull.await();  // await 操作释放了锁，条件满足被唤醒，再次获取所
            }
            items[tail] = t;
            if (++tail == getCapacity()) {
                tail = 0;
            }
            ++count;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();

        try {
            while (count == 0) { // take 取： 队列为空时，则挂起 (await)， 直到收到 notEmpty的信号
                System.out.println("take wait...");
                notEmpty.await(); // await 操作释放了锁，条件满足被唤醒，再次获取所
            }
            T ret = items[head];
            items[head] = null;

            if (++head == getCapacity()) {
                head = 0;
            }
            --count;
            notFull.signalAll();

            return ret;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();

        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    public int getCapacity() {
        return items.length;
    }
}
