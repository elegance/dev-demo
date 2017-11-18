package org.orh.sigleton;
public class SigletonTest1 {

    public static void main(String[] args) {
        System.out.println("main begin" );
        
        // 静态资源初始化本身就是 单线程的(同步阻塞)，在类内部资源被初次访问时，触发静态初始化,初始化的顺序 是从上往下.
        // 被静态初始化“阻塞”的方法，不是阻塞"BLOCKD"状态，而是 "RUNNABLE" 状态
        
        // 没有直接打印出 "doOther"
        new Thread(() -> MyObject.doOther(), "thread-MyObject-doOther-1").start(); // 访问静态方法会触发 同步的“静态初始化”，初始化的顺序 ："静态代码块1" ==> "myObject实例化(执行构造函数)" ==> "静态代码块2" ==> "静态方法初始化 getInstance、doOther "
        
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode() + ""), "thread-1").start(); // “阻塞等待”，但是线程呈 `RUNNABLE`状态
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode() + ""), "thread-2").start(); // “阻塞等待”，但是线程呈 `RUNNABLE`状态 
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode() + ""), "thread-3").start(); // “阻塞等待”，但是线程呈 `RUNNABLE`状态

        new Thread(() -> MyObject.doOther(), "thread-MyObject-doOther-2").start(); // 访问 MyObject的其他静态方法 ，也会被阻塞   ★★ ==> MyObject.class 被锁
        
        System.out.println("main   end");
    }
    
    static class MyObject {

        
        static {
            System.out.println("静态代码块1执行开始");
            sleep(1000);
            System.out.println("静态代码块1执行结束");
        }
        
        // 立即加载 == 饿汉模式
        public static MyObject myObject = new MyObject();

        static {
            System.out.println("静态代码块2执行开始");
            sleep(1000);
            System.out.println("静态代码块2执行结束");
        }

        private MyObject() {
            System.out.println("构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(5000);
            System.out.println("构造函数执行结束");
        }

        public static MyObject getInstance() {
            // 特点：
            // 此类 不能有其它实例变量
            // getInstance()方法没有同步，可能出现非线程安全问题？ -- 真想不出哪里有线程安全问题，认为
            System.out.println("getInstance()方法执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(1000);
            return myObject;
        }
        
        public static void doOther() {
            System.out.println("doOther...");
        }
    }
    
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
