public class VolatileCompare {

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                service.methodNormal();
            }
        }, "thread-A");

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                service.methodVolatile();
            }
        }, "thread-B");

        threadA.start();
        threadB.start();

        Thread.sleep(1000);
        service.setRunning(false); // 停止threadA信号
        service.setRunningVolatile(false); // 停止threadB信号
        System.out.println("主线程发送停止信号完毕。");
    }

    static class Service {
        private boolean isRunning = true;
        volatile private boolean isRunningVolatile = true;

        public void methodNormal() {
            System.out.println("进入了 methodNormal...");
            while (isRunning) {
            }
            System.out.println("线程停止，离开methodNormal...");
        }

        public void methodVolatile() {

            System.out.println("进入了 methodVolatile...");
            while (isRunningVolatile) {
            }
            System.out.println("线程停止，离开methodVolatile...");
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        public boolean isRunningVolatile() {
            return isRunningVolatile;
        }

        public void setRunningVolatile(boolean isRunningVolatile) {
            this.isRunningVolatile = isRunningVolatile;
        }

    }
}
