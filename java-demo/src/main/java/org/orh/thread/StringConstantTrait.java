package org.orh.thread;
public class StringConstantTrait {
    public static void main(String[] args) {
        String a = "a";
        String b = "a";
        
        System.out.println(a == b); // true，引用相同
        
        Thread threadA = new Thread(() -> {
            String lockStr = "a";
            Service.pirnt(lockStr);
        }, "thread-A");

        Thread threadB = new Thread(() -> {
            String lockStr2 = "a";
            Service.pirnt(lockStr2); // lockStr2 其实还是指向常量池中的“a”,所以会被线程A阻塞，与期望不配
        }, "thread-B");
        
        threadA.start();
        threadB.start();
    }
    
    static class Service {
        public static void pirnt(String param) {
            try {
                synchronized(param) {
                    while(true) {
                        System.out.println(Thread.currentThread().getName());
                        Thread.sleep(1000);
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
