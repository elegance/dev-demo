package org.orh.lock.CAS;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABA {
    private static AtomicInteger atomicInt = new AtomicInteger(100);

    private static AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference<Integer>(100, 0);

    public static void main(String[] args) throws InterruptedException {
        Thread intT1 = new Thread(() -> {
            atomicInt.compareAndSet(100, 101);
            atomicInt.compareAndSet(101, 100);
        });

        Thread intT2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean c3 = atomicInt.compareAndSet(100, 101);
            System.out.println(c3); // true, 设置成功了 （此时你要注意，你的 atomicInt 中间已经被改过了，如果你不关心你这个“ABA”的问题，则可以忽略，否则你需要使用下面的AtomicStampedReference<V>）
        });

        intT1.start();
        intT2.start();
        intT1.join();
        intT2.join();

        Thread refT1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicStampedRef.compareAndSet(100, 101, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
            atomicStampedRef.compareAndSet(101, 100, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
        });

        Thread refT2 = new Thread(() -> {
            int stamp = atomicStampedRef.getStamp();
            System.out.println("before sleep: stamp = " + stamp); // ==> 0
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("after sleep: stamp = " + atomicStampedRef.getStamp()); // ==> 2
            boolean c3 = atomicStampedRef.compareAndSet(100, 101, stamp, stamp + 1);
            System.out.println(c3); // false, 设置失败了，因为戳“stamp”的检查未通过
        });
        
        refT1.start();
        refT2.start();
    }
}