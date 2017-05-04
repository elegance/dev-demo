public class SuspendResumeTest extends Thread {

    private long i = 0;

    public long getI() {
        return i;
    }

    public void setI(long i) {
        this.i = i;
    }

    @Override
    public void run() {
        while (true) {
            i++;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        SuspendResumeTest thread = new SuspendResumeTest();
        thread.start();
        Thread.sleep(2000);
        
        // A段
        thread.suspend();
        System.out.printf("A=%d i=%d\n", System.currentTimeMillis(), thread.getI());
        Thread.sleep(2000);
        System.out.printf("A=%d i=%d\n", System.currentTimeMillis(), thread.getI());
        
        // B段
        thread.resume();
        Thread.sleep(2000);
        
        // C段
        thread.suspend();
        System.out.printf("A=%d i=%d\n", System.currentTimeMillis(), thread.getI());
        Thread.sleep(2000);
        System.out.printf("A=%d i=%d\n", System.currentTimeMillis(), thread.getI());
    }
}
