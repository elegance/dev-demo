public class YieldTest {

    public static void main(String args[]) {
        new Thread(() -> {
            long beginTime = System.currentTimeMillis();
            int count = 0;

            for (int i = 0; i < 50000000; i++) {
                // Thread.yield();
                count = count + (i + 1);
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("用时：%d 毫秒", (endTime - beginTime));
        }).start();
    }
}
