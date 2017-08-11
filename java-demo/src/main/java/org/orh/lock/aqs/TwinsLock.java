package org.orh.lock.aqs;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 设计一个同步工具，该工具在同一时刻，只能有两个线程能够并行访问，超过限制的其他线程进入阻塞状态
 *
 */
public class TwinsLock implements Lock, Serializable {

    private static final long serialVersionUID = 8934172281875523421L;

    // 内部类，自定义同步器
    private static class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = -6788242375317473486L;

        {
            setState(2);
        }

        // 是否处于占用状态
        @Override
        protected boolean isHeldExclusively() {
            return getState() < 2;
        }

        @Override
        protected boolean tryAcquire(int acquires) {
            if (acquires != 1) {
                return false;
            }
            int currentStats = getState();
            if (currentStats <= 0) {
                return false;
            }
            if (compareAndSetState(currentStats, currentStats - 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 释放锁，将状态设置为0
        @Override
        protected boolean tryRelease(int releases) {
            if (releases != 1) {
                return false;
            }
            for (;;) {
                int currentStats = getState();
                if (compareAndSetState(currentStats, currentStats + 1)) {
                    setExclusiveOwnerThread(null);
                    return true;
                }
            }
        }

        // 返回一个Condition，每个condition都包含了一个condition队列
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    // 仅需将操作代理到 Sync上即可
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

}
