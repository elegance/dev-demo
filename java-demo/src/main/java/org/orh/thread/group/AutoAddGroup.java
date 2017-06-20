package org.orh.thread.group;

public class AutoAddGroup {
    public static void main(String[] args) {
        System.out.println("A 处线程：" + Thread.currentThread().getName() + " 所属线程组名称为：" + Thread.currentThread().getThreadGroup().getName()
                + " 中有线程组数量：" + Thread.currentThread().getThreadGroup().activeGroupCount());
        
        ThreadGroup group1 = new ThreadGroup("新的线程组1"); //自动归属 main组中
        ThreadGroup group2 = new ThreadGroup("新的线程组2"); //自动归属 main组中

        System.out.println("B 处线程：" + Thread.currentThread().getName() + " 所属线程组名称为：" + Thread.currentThread().getThreadGroup().getName()
                + " 中有线程组数量：" + Thread.currentThread().getThreadGroup().activeGroupCount());
        
        ThreadGroup[] groups = new ThreadGroup[Thread.currentThread().getThreadGroup().activeGroupCount()];
        Thread.currentThread().getThreadGroup().enumerate(groups);
        for (int i = 0; i < groups.length; i++) {
            System.out.println("第" + (i+1) + "个线程组的名称为【" + groups[i].getName() + "】");
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
