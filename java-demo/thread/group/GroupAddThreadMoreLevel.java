package group;

public class GroupAddThreadMoreLevel {
    public static void main(String[] args) {
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
        
        ThreadGroup mainChildAGroup = new ThreadGroup(mainGroup,"mainChildAGroup");

        new Thread(mainChildAGroup, () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " 开始执行...");
            sleep(2000);
            System.out.println(threadName + " 执行结束.");
        }, "thread-mainChildAGroup-1").start();
        
        // activeGroupCount、activeCount 的值是不固定的，是系统的一个即时快照
        ThreadGroup[] listGroup = new ThreadGroup[Thread.currentThread().getThreadGroup().activeGroupCount()];
        Thread.currentThread().getThreadGroup().enumerate(listGroup);
        
        System.out.printf("main线程中有多少个子线程组：%d， 名字为：%s \n", listGroup.length, listGroup[0].getName());
        
        Thread[] threads = new Thread[listGroup[0].activeCount()];
        listGroup[0].enumerate(threads);
        
        System.out.printf("线程组：%s 下有线程 %d 个，线程名称为：%s\n", threads[0].getThreadGroup().getName(), threads[0].getThreadGroup().activeCount(), threads[0].getName());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
