/**
 * 1. 演示 StringBuilder 非线程安全，StringBuffer线程安全 
 * 2. 演示方法内的私有变量也不一定是线程安全
 */
public class MethodVaribleSecurity {

    public static void main(String[] args) {
        // 定义两个 方法内的 “局部”变量
        StringBuilder sbd = new StringBuilder();
        StringBuffer sbf = new StringBuffer();

        ThreadGroup testGroup = new ThreadGroup("testGroup");

        for (int i = 0; i < 100; i++) {
            new Thread(testGroup, () -> { // 方法内建立多个线程并启动
                for (int j = 0; j < 10; j++) {
                    sbd.append("A"); // 多个子线程在改变 父线程 的“局部”变量，此时的局部就成了 共享变量了
                    sbf.append("B");
                    sleep(10);
                }
            }).start();
        }

        while (testGroup.activeCount() > 0) {
            sleep(0);
        }

        // 输出结果：buffer 恒定长度 1000，而builder 的长度则不是，builder 长度小于1000，运行有时还会抛出错误
        System.out.printf("builder length: %d, buffer length： %d", sbd.length(), sbf.length());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
