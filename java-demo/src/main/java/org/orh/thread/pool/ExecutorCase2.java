package org.orh.thread.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorCase2 {
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Future<String> future = executor.submit(new Task()); // 提交异步任务
        System.out.println("do other things");
        
        String result = future.get(); // get 会等待任务的返回， 主线程进入 WAITING 状态，下面代码不会执行
        System.out.println("asynchronus result:" + result);
    }

    static class Task implements Callable<String> {

        public String call() throws Exception {
            System.out.println("异步任务开始执行...");
            TimeUnit.SECONDS.sleep(2); // 睡眠2s，模拟异步耗时
            return "this is future case";
        }
    }
}
