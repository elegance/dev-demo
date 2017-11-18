package org.orh.thread.group;

public class GroupAddGroup {
    public static void main(String[] args) {
        System.out.println("线程组名称：" + Thread.currentThread().getThreadGroup().getName());
        System.out.println("线程组中活动数量的线程数：" + Thread.currentThread().getThreadGroup().activeCount());
        
        System.out.println("线程组中线程组的数量 - 加之前：" + Thread.currentThread().getThreadGroup().activeGroupCount());
        ThreadGroup newGroup = new ThreadGroup(Thread.currentThread().getThreadGroup(), "newGroup"); // 构造函数显示的方式指定了父线程组
        System.out.println("线程组中线程组的数量 - 加之后：" + Thread.currentThread().getThreadGroup().activeGroupCount());
    }
}
