package org.orh.thread;
public class SyncNotExtends {
    
    public static void main(String[] args) {
        SubService service = new SubService();
        
        Thread threadA = new Thread(() -> {
            service.serviceMethod();
        }, "threadA☆");
        
        Thread threadB = new Thread(() -> {
            service.serviceMethod();
        }, "threadB★");
        
        threadA.start();
        threadB.start();
    }

    static class BaseService {
        synchronized public void serviceMethod() {
            try {
                System.out.printf("BaseService serviceMethod，下一步 sleep begin threadName=%s time=%d\n", Thread.currentThread().getName(), System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.printf("BaseService serviceMethod，下一步 sleep end threadName=%s time=%d\n", Thread.currentThread().getName(), System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class SubService extends BaseService {
        
        @Override // 重写时去掉了 synchronized 标识
        public void serviceMethod() {
            try {
                System.out.printf("SubService serviceMethod，下一步 sleep begin threadName=%s time=%d\n", Thread.currentThread().getName(), System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.printf("SubService serviceMethod，下一步 sleep end threadName=%s time=%d\n", Thread.currentThread().getName(), System.currentTimeMillis());
                
                super.serviceMethod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
