package org.orh.thread;
public class DeadLockTest {

    public static void main(String[] args) {
        Service homeService = new Service();

        Thread aMaoThread = new Thread(() -> {
            try {
                homeService.toTheRestRoom();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "阿毛");

        Thread aMaoDadThread = new Thread(() -> {
            try {
                homeService.toTheRestRoom();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "阿毛他爹");

        Thread aMaoMomThread = new Thread(() -> {
            try {
                homeService.toTheRestRoom();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "阿毛麻麻");

        aMaoThread.start();
        aMaoDadThread.start();
        aMaoMomThread.start();
    }

    static class Service {
        private Object aRollPaper = new Object(); // 厕所公共资源 ：一卷卫生纸
        private Object aPitPosition = new Object(); // 厕所公共资源：一个坑位

        public void toTheRestRoom() throws InterruptedException {
            String name = Thread.currentThread().getName();

            if (name.equals("阿毛")) { // 阿毛这娃每次都比较着急，上厕所 先抢着占坑位，根本不拿纸(等完事叫别人送进来，或者撅着腚跑出来拿纸)
                System.out.printf("【%s】 准备上厕所\n 1. 上坑位\n 2. 拿纸\n", name);
                synchronized (aPitPosition) { // 占坑位
                    System.out.printf("【%s】 进入[坑位]开始上厕所\n", name);
                    Thread.sleep(3000);

                    System.out.printf("【%s】 蹲坑完毕，尝试要[纸]...\n", name);
                    System.out.printf("【%s】 Oh， shit 好像纸被人拿了 ，我等啊等啊...（-_-|||）\n", name);

                    synchronized (aRollPaper) { // 得到公共的那卷纸
                        System.out.printf("【%s】 尝试拿[纸]\n", name);
                        Thread.sleep(1000);
                    }

                }
            } else {
                System.out.printf("【%s】 准备上厕所\n 1. 拿纸\n 2. 上坑位\n", name);
                // 其他正常的人 都是，拿着纸，进坑位的
                synchronized (aRollPaper) { // 得到公共的那卷纸
                    System.out.printf("【%s】 尝试拿[纸]\n", name);
                    Thread.sleep(1000);
                    System.out.printf("【%s】 拿到到[纸]了，\n", name);

                    // if aPitPosition is locked : 这里我们我以看出 synchronized的局限性，无法“做判断”
                    // ，不能以尝试那锁、或者指定等待时间，下面信息输出也不好加判断
                    System.out.printf("【%s】 ？ 坑位居然有人，进不去...((＠￣ー￣＠) 每次都有人不拿纸就进去，看来该教育教育了...)\n", name);

                    synchronized (aPitPosition) { // 占坑位
                        System.out.printf("【%s】 进入[坑位]开始上厕所\n", name);
                        Thread.sleep(3000);
                    }
                }
            }
            System.out.printf("【%s】上完厕所了。", name);
        }
    }
}
