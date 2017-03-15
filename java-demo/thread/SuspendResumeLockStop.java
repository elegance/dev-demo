import java.util.logging.Logger;

public class SuspendResumeLockStop {

    public static void main(String [] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            private long i = 0;
            
            @Override
            public void run() {
                while(true) {
                    i++;
                     System.out.println(i); // println内部使用同步对象
                }
            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.suspend(); // 造成公共同步对象被上面独占
        
        Logger log = Logger.getGlobal();
        log.info("这里被执行。。。。");
        
        System.out.println("main end!"); // 此处无法访问println内的同步对象，导致阻塞
        
        log.info("阻塞无法被执行。。。。，前面的（main end！）兄弟也输出不了");
    }
}
