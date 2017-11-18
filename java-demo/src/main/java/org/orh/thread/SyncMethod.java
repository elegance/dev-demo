package org.orh.thread;
import java.util.concurrent.TimeUnit;

public class SyncMethod {
    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();

        Thread threadA = new Thread(() -> {
            TimeBean.beginTime1 = System.currentTimeMillis();
            task.doLongTimeTask();
            TimeBean.endTime1 = System.currentTimeMillis();
        }, "threadA");

        Thread threadB = new Thread(() -> {
            TimeBean.beginTime2 = System.currentTimeMillis();
            task.doLongTimeTask();
            TimeBean.endTime2 = System.currentTimeMillis();
        }, "threadB");

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        long beginTime = Math.min(TimeBean.beginTime1, TimeBean.beginTime2); // 线程 谁先真正 设置开始时间的不确定性，取最小值
        long endTime = Math.max(TimeBean.endTime1, TimeBean.endTime2); // 线程 谁先运行结束的时间的不确定性，取最大值
        System.out.printf("耗时: %d秒\n", TimeUnit.MILLISECONDS.toSeconds((endTime - beginTime)));
    }

    static class Task {
        private String getData1;
        private String getData2;

        public synchronized void doLongTimeTask() {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.printf("%s begin task\n", threadName);
                TimeUnit.SECONDS.sleep(3);

                getData1 = String.format("从DB长时间查询返回的值 1 threadName=%s", Thread.currentThread().getName());
                getData2 = String.format("从DB长时间查询返回的值 2 threadName=%s", Thread.currentThread().getName());

                System.out.println(getData1);
                System.out.println(getData2);

                System.out.printf("%s end task\n", threadName);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TimeBean {
        public static long beginTime1;
        public static long endTime1;
        public static long beginTime2;
        public static long endTime2;
    }
}
