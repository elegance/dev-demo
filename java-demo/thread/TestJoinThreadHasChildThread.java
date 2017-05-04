public class TestJoinThreadHasChildThread {

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(() -> {
            new Thread(() -> {
                sleep(10 * 10000);
                System.out.println("waitThread-end");
            }).start();

            sleep(2000);
            System.out.println("waitThread-end");
        });

        waitThread.start();
        waitThread.join();
        System.out.println("main end!");
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
