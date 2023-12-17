package org.orh.zookeeper.curator;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * LockTest
 *
 * @author ouronghui
 * @since 2023/12/13 15:48
 */
public class LockTest {

    private static final Map<String, InterProcessMutex> DISTRIBUTED_LOCK_MAP = new ConcurrentHashMap<>();

    static CuratorFramework curator;
    static CuratorFramework qaCurator;

    static {
        curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:8181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(3000)
                .namespace("lock-test")
                .build();
        curator.start();


    }

    @SneakyThrows
    public static void prodLock() {
        System.setProperty("zookeeper.sasl.client", "false");

        qaCurator = CuratorFrameworkFactory.builder()
                .connectString("10.1.3.157:2181,10.1.4.157:2181,10.1.4.158:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(3000)
                .namespace("canal-adapter")
                .build();
        qaCurator.start();
//        prodCurator.delete().forPath("/sync-etl/-" + "61xcyd41385233ordv10v10f");

        InterProcessMutex lock = getRemoteLockProd("/sync-etl/-" + "61xcyd41385233ordv10v10f");
        boolean acquire = lock.acquire(500, TimeUnit.MILLISECONDS);
        System.out.println(acquire);
        sleep(Long.MAX_VALUE);

    }

    private static final Map<String, InterProcessMutex> DISTRIBUTED_LOCK = new ConcurrentHashMap<>();

    private static InterProcessMutex getRemoteLockProd(String key) {
        InterProcessMutex lock = DISTRIBUTED_LOCK.get(key);
        if (lock == null) {
            synchronized (LockTest.class) {
                lock = DISTRIBUTED_LOCK.get(key);
                if (lock == null) {
                    lock = new InterProcessMutex(qaCurator, key);
                    DISTRIBUTED_LOCK.put(key, lock);
                }
            }
        }
        return lock;
    }

    @SneakyThrows
    public static void main(String[] args) {
        if (true) {
            prodLock();
            return;
        }
        boolean acquireKey1 = false, acquireKey2 = false;
        String key1 = "/lock/key1", key2 = "/lock/key2";
        try {
            acquireKey1 = getRemoteLock(key1).acquire(500, TimeUnit.MILLISECONDS);
            acquireKey2 = getRemoteLock(key2).acquire(500, TimeUnit.MILLISECONDS);
            System.out.println("/acquireKey1: " + acquireKey1);
            System.out.println("acquireKey2: " + acquireKey2);


            System.out.println(getRemoteLock(key1).acquire(500, TimeUnit.MILLISECONDS));
            sleep(10 * 1000);
            getRemoteLock(key1).release();
            System.out.println("release key1");
            acquireKey1 = getRemoteLock(key1).acquire(500, TimeUnit.MILLISECONDS);
            sleep(10*1000);
            getRemoteLock(key1).release();
            System.out.println("release key1 again");

            sleep(Long.MAX_VALUE);

        } finally {
            if (acquireKey1) {
                getRemoteLock(key1).release();
            }
            if (acquireKey2) {
                getRemoteLock(key1).release();
            }
        }
    }

    @SneakyThrows
    private static void sleep(long mill) {
        TimeUnit.MILLISECONDS.sleep(mill);
    }

    private static InterProcessMutex getRemoteLock(String key) {
        InterProcessMutex lock = DISTRIBUTED_LOCK_MAP.get(key);
        if (lock == null) {
            synchronized (LockTest.class) {
                lock = DISTRIBUTED_LOCK_MAP.get(key);
                if (lock == null) {
                    lock = new InterProcessMutex(curator, key);
                    DISTRIBUTED_LOCK_MAP.put(key, lock);
                }
            }
        }
        return lock;
    }
}
