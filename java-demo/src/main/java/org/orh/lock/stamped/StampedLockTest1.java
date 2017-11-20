package org.orh.lock.stamped;

import java.util.concurrent.locks.StampedLock;

public class StampedLockTest1 {

    static class Point {

        private double x, y;

        private StampedLock stampedLock = new StampedLock();

        void move(double deltaX, double deltaY) {
            long stamp = stampedLock.writeLock(); // 写锁
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                stampedLock.unlockWrite(stamp); // 释放写锁
            }
        }
        
        double distanceFromOrigin() {
            long stamp = stampedLock.tryOptimisticRead();
            
            double currentX = x, currentY = y;
            
            if (!stampedLock.validate(stamp)) {
                stamp = stampedLock.readLock();
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }
    }
}
