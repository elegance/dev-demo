package org.orh.thread;
public class SyncStaticMethod {

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            Service.printA(); // 不需要Service的实例即可调用静态方法
        }, "threadA");

        Thread threadB = new Thread(() -> {
            Service.printA(); // 不需要Service的实例即可调用静态方法，同样会被阻塞
        }, "threadB");

        Service service = new Service();
        Thread threadC = new Thread(() -> {
            service.printB(); // printB的同步方法不受A、B的影响，锁的对象不同==> synchronized static方法 锁方法所在class
        }, "thread-C");

        Thread threadD = new Thread(() -> {
            service.printC(); // printC同步方法内部使用synchronized(Service.class)，所以会被A、B所阻塞
        }, "thread-D");

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
        // 最终结果：A、B、D 同步阻塞，C线程锁的service实例，无争锁，异步执行
    }

    static class Service {
        synchronized public static void printA() {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.printf("线程名称为：%s 在%d 进入printA\n", threadName, System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.printf("线程名称为：%s 在%d 离开printA\n", threadName, System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        synchronized public void printB() {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.printf("线程名称为：%s 在%d 进入printB\n", threadName, System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.printf("线程名称为：%s 在%d 离开printB\n", threadName, System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void printC() {
            synchronized (Service.class) {
                try {
                    String threadName = Thread.currentThread().getName();
                    System.out.printf("线程名称为：%s 在%d 进入printC\n", threadName, System.currentTimeMillis());
                    Thread.sleep(1000);
                    System.out.printf("线程名称为：%s 在%d 离开printC\n", threadName, System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
