package group;

public class GroupRecurse {

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup aGroup = new ThreadGroup(mainGroup, "aGroup");

        System.out.println("activeGroupCount:" + Thread.currentThread().getThreadGroup().activeGroupCount() + " [增加子组会感知]");
        
        ThreadGroup bGroup = new ThreadGroup(aGroup, "bGroup");

        System.out.println("activeGroupCount:" + Thread.currentThread().getThreadGroup().activeGroupCount() + " [增加孙子组也会感知]");

        System.out.println("==========递归式===========");
        // 申请数组空间
        ThreadGroup[] listGroup1 = new ThreadGroup[Thread.currentThread().getThreadGroup().activeGroupCount()];

        // 传入true 是递归取得子组级子孙组
        Thread.currentThread().getThreadGroup().enumerate(listGroup1, true);

        for (int i = 0; i < listGroup1.length; i++) {
            ThreadGroup threadGroup = listGroup1[i];
            if (threadGroup != null) {
                System.out.println(threadGroup.getName());
            }
        }
        
        System.out.println("==========非递归式===========");

        // 申请数组空间，空间不一定全部用完
        ThreadGroup[] listGroup2 = new ThreadGroup[Thread.currentThread().getThreadGroup().activeGroupCount()];

        // 非递归
        Thread.currentThread().getThreadGroup().enumerate(listGroup2, false);

        for (int i = 0; i < listGroup2.length; i++) {
            ThreadGroup threadGroup = listGroup2[i];
            if (threadGroup != null) {
                System.out.println(threadGroup.getName());
            }
        }
    }
}
