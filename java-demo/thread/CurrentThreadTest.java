public class CurrentThreadTest extends Thread {
    
    public CurrentThreadTest() {
        System.out.println("CountOperate constructor ----begin");
        System.out.printf("Thread.currentThread().getName() = %s \n", Thread.currentThread().getName());
        System.out.printf("this.getName() = %s \n", this.getName());
        System.out.println("CountOprate constructor ----end");
    }
    
    @Override
    public void run() {
        System.out.println("run method ----begin");
        System.out.printf("Thread.currentThread().getName() = %s \n", Thread.currentThread().getName());
        System.out.printf("this.getName() = %s \n", this.getName());
        System.out.println("run method ----end");
    }
    
    public static void main(String[] args) throws InterruptedException {
        CurrentThreadTest c = new CurrentThreadTest();
        Thread t1 = new Thread(c);
        
        t1.setName("Wrap-thread");
        t1.start();
        
        t1.join();
        
        c.setName("Thread-self");
        c.start();
        
        
    }
}
