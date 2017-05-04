import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * 测试 synchronized 方法 与 DCL(Double Check Lock) 实现单例方法的对比：
 * <h3>结论：</h3>
 * <ol>
 * <li>场景1，"初始高比发访问，触发并发调用实例化"，sync方法略快于DCL，但是差异很小</li>
 * <li>场景2，"初始单个触发调用实例化，后高比发访问"，表现一致，几乎没有差别</li>
 * <li>那么：其实如果方法在内部只是判断、返回，同步与非同步几乎不会对性能造成影响</li>
 * </ol>
 * 
 * <h3>测试结果：</h3>
 * <ol>
 * <li>初始高比发访问，触发并发调用实例化：<br>
 * 
 * <pre>
 * synMethod: 3296millis<br>
 * dclMethod: 3405millis
 * </pre>
 * 
 * </li>
 * 
 * <li>初始单个触发调用实例化，后高比发访问：<br>
 * synMethod: 734millis<br>
 * dclMethod: 734millis</li>
 * </ol>
 * <h3>测试环境：
 * <h3>
 * 
 * <pre>
 * java version "1.8.0_45"
 * Java(TM) SE Runtime Environment (build 1.8.0_45-b15)
 * Java HotSpot(TM) 64-Bit Server VM (build 25.45-b02, mixed mode)
 * 
 * win7 64bit/eclipse 4.6.1
 * </pre>
 */
public class SyncDclMethodCompare {
    private static int concurrency = 10000;

    public static void main(String[] args) throws InterruptedException {
        // 用来记录每个场景、每种方式的时间
        TimeBean scene1SyncTimes = new TimeBean();
        TimeBean scene1DCLTimes = new TimeBean();

        TimeBean scene2SyncTimes = new TimeBean();
        TimeBean scene2DCLTimes = new TimeBean();

        // 用不同场景，来展示不同方式的优、缺点

        // 1. 初始高并发取实例-触发高并发初始化访问
        Thread scene1Thread = new Thread(() -> {
            startScene1(scene1SyncTimes, scene1DCLTimes);
        }, "secne-1");
        scene1Thread.start();
        scene1Thread.join(); // 场景1 完毕后，才开始 场景2的测试 【join
                             // 是不理会scene1Thread中的子线程的，要在scene1Thread内部处理好：join之后代表内部包括子线程都已经运行完毕】

        MyObject.reset(); // 重置对象，让其重新初始化
        DCLMyObject.reset();

        // 2. 单个初始化，然后高并发取实例
        Thread scene2Thread = new Thread(() -> {
            startScene2(scene2SyncTimes, scene2DCLTimes);
        }, "secne-2");
        scene2Thread.start();
        scene2Thread.join();

        System.out.println("================执行结果=======================");

        printRs("高并发初始化访问-synchronized Method：", scene1SyncTimes.beginTimeList, scene1SyncTimes.endTimeList);
        printRs("高并发初始化访问-DCL Method：", scene1DCLTimes.beginTimeList, scene1DCLTimes.endTimeList);

        printRs("高并发取实例-synchronized Method：", scene2SyncTimes.beginTimeList, scene2SyncTimes.endTimeList);
        printRs("高并发取实例-DCL Method：", scene2DCLTimes.beginTimeList, scene2DCLTimes.endTimeList);
    }

    /**
     * 打印结果
     */
    public static void printRs(String rsName, List<Long> beginTimeList, List<Long> endTimeList) {
        long begin = Collections.min(beginTimeList);
        long end = Collections.max(endTimeList);
        System.out.printf("%-30s : %d millis\n", rsName, (end - begin));
    }

    /**
     * 初始高并发取实例-触发高并发初始化访问
     */
    public static void startScene1(TimeBean scene1SyncTimes, TimeBean scene1DCLTimes) {
        ThreadGroup groupScene1 = new ThreadGroup("groupScene1");
        for (int i = 0; i < concurrency; i++) {
            new Thread(groupScene1, () -> {
                scene1SyncTimes.beginTimeList.add(System.currentTimeMillis());
                MyObject.getInstance();
                scene1SyncTimes.endTimeList.add(System.currentTimeMillis());
                System.out.println(Thread.currentThread().getName() + " over!");
            }, "thread-scene1-syncMethod-" + i).start();

            new Thread(groupScene1, () -> {
                scene1DCLTimes.beginTimeList.add(System.currentTimeMillis());
                DCLMyObject.getInstance();
                scene1DCLTimes.endTimeList.add(System.currentTimeMillis());
                System.out.println(Thread.currentThread().getName() + " over!");
            }, "thread-scene1-dclMethod-" + i).start();
        }
        while (groupScene1.activeCount() > 0) {
            sleep(0);
        }
    }

    /**
     * 单个初始化，然后高并发取实例
     */
    public static void startScene2(TimeBean scene2SyncTimes, TimeBean scene2DCLTimes) {
        ThreadGroup groupScene2 = new ThreadGroup("groupScene2");
        // 单个初始化
        Thread initThread = new Thread(groupScene2, () -> {
            MyObject.getInstance();
            DCLMyObject.getInstance();
        }, "thread-init");
        initThread.start();
        try {
            initThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 下面的线程都属于 直接取实例
        for (int i = 0; i < concurrency; i++) {
            new Thread(groupScene2, () -> {
                scene2SyncTimes.beginTimeList.add(System.currentTimeMillis());
                MyObject.getInstance();
                scene2SyncTimes.endTimeList.add(System.currentTimeMillis());
                System.out.println(Thread.currentThread().getName() + " over!");
            }, "thread-scene2-syncMethod-" + i).start();

            new Thread(groupScene2, () -> {
                scene2DCLTimes.beginTimeList.add(System.currentTimeMillis());
                DCLMyObject.getInstance();
                scene2DCLTimes.endTimeList.add(System.currentTimeMillis());
                System.out.println(Thread.currentThread().getName() + " over!");
            }, "thread-scene2-dclMethod-" + i).start();
        }

        while (groupScene2.activeCount() > 0) {
            sleep(0);
        }
    }

    static class TimeBean {
        List<Long> beginTimeList = new Vector<Long>();
        List<Long> endTimeList = new Vector<Long>();
    }

    static class MyObject {
        public static MyObject myObject;

        private MyObject() {
            sleep(1000); // 模拟 实例化对象前的耗时准备工作
            System.out.println("构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(2000);
            System.out.println("构造函数执行结束");
        }

        synchronized public static MyObject getInstance() {
            if (myObject == null) {
                myObject = new MyObject();
            }
            return myObject;
        }

        public static void reset() {
            myObject = null;
        }
    }

    static class DCLMyObject {
        private static DCLMyObject dclMyObject;

        private DCLMyObject() {
            System.out.println("构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(2000);
            System.out.println("构造函数执行结束");
        }

        public static DCLMyObject getInstance() {
            if (dclMyObject == null) {
                sleep(1000); // 模拟 实例化对象前的耗时准备工作，如果这个准备工作对外部资源消耗过大，可以考虑移动入同步块，能保证只被一次调用
                synchronized (DCLMyObject.class) {
                    if (dclMyObject == null) {
                        dclMyObject = new DCLMyObject();
                    }
                }
            }
            return dclMyObject;
        }

        public static void reset() {
            dclMyObject = null;
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
