package group;

public class GroupInnerStop {

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup myGroup = new ThreadGroup("myGroup");

        for (int i = 0; i < 5; i++) {
            new Thread(myGroup, () -> {
                System.out.println("ThreadName=" + Thread.currentThread().getName() + " 准备开始死循环了：");
                while (!Thread.currentThread().isInterrupted()) {
                }
                System.out.println("ThreadName=" + Thread.currentThread().getName() + " 结束了！");
            }, "thread-myGroup-" + i).start();
        }

        Thread.sleep(5 * 1000);
        myGroup.interrupt();
        System.out.println("group 调用了interrupt 方法");
    }
}
