public class WaitNotifyInsert {

    public static void main(String[] args) {
        Service service = new Service();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> service.backupA(), "backup-a" + i).start();
            new Thread(() -> service.backupB(), "backup-b" + i).start();
        }
    }

    static class Service {

        volatile boolean preIsARun = false;

        synchronized public void backupA() {
            try {
                while (preIsARun) {
                    this.wait();
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println("★★★★★");
                }
                preIsARun = true;
                notifyAll();
            } catch (Exception e) {
            }
        }

        synchronized public void backupB() {
            try {
                while (!preIsARun) {
                    this.wait();
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println("☆☆☆☆☆");
                }
                preIsARun = false;
                notifyAll();
            } catch (Exception e) {
            }
        }
    }
}
