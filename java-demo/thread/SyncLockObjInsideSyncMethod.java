public class SyncLockObjInsideSyncMethod {

    public static void main(String[] args) {
        Service service = new Service();
        MyObject comObject = new MyObject();

        Thread thread1 = new Thread(() -> {
            service.testMethod1(comObject);
        }, "thread1");

        Thread thread2 = new Thread(() -> {
            comObject.speedPringString();
        }, "thread2");
        
        Thread thread3 = new Thread(() -> {
            comObject.speedPringString2();
        }, "thread3");
        
        thread1.start();
        thread2.start();
        thread3.start();
    }

    static class Service {
        public void testMethod1(MyObject lockObj) {
            synchronized (lockObj) {
                try {
                    String threadName = Thread.currentThread().getName();
                    System.out.printf("testMethod1 ___getLock time=%d run threadName=%s\n", System.currentTimeMillis(), threadName);
                    Thread.sleep(1000);
                    System.out.println("----------------------------");
                    System.out.printf("testMethod1 ___releaseLock time=%d run threadName=%s\n", System.currentTimeMillis(), threadName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class MyObject {
        synchronized public void speedPringString() {
            String threadName = Thread.currentThread().getName();
            System.out.printf("speedPringString(synchronized method) ___getLock time=%d run threadName=%s\n", System.currentTimeMillis(), threadName);
            System.out.println("----------------------------");
            System.out.printf("speedPringString(synchronized method) ___releaseLock time=%d run threadName=%s\n", System.currentTimeMillis(), threadName);
        }
        
        public void speedPringString2() {
            synchronized(this) {
                String threadName = Thread.currentThread().getName();
                System.out.printf("speedPringString2(synchronized(this)) ___getLock time=%d run threadName=%s\n", System.currentTimeMillis(), threadName);
                System.out.println("----------------------------");
                System.out.printf("speedPringString2(synchronized(this)) ___releaseLock time=%d run threadName=%s\n", System.currentTimeMillis(), threadName);
            }
        }
    }

}
