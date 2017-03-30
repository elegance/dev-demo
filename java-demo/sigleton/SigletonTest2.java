public class SigletonTest2 {

    public static void main(String[] args) {
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-syncMethod-1").start();
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-syncMethod-2").start();
        new Thread(() -> System.out.println(MyObject.getInstance().hashCode()), "thread-syncMethod-3").start();
       
        new Thread(() -> System.out.println(MyObject.getDCLObjInstance().hashCode()), "thread-dclMethod-1").start();
        new Thread(() -> System.out.println(MyObject.getDCLObjInstance().hashCode()), "thread-dclMethod-2").start();
        new Thread(() -> System.out.println(MyObject.getDCLObjInstance().hashCode()), "thread-dclMethod-3").start();
    }
    static class MyObject {
        public static MyObject myObject;

        private MyObject() {
            System.out.println("构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(2000);
            System.out.println("构造函数执行结束");
        }

        synchronized // 标识方法为同步方法，在[实例化] 返回对象前，只能有单个线程访问，其他线程等待
        public static MyObject getInstance() {
            sleep(1000); // 模拟 实例化对象前的耗时准备工作
            if (myObject == null) { // 当没有`synchronize`锁时，明显这里的判断是非线程安全的，因此会有多个线程在 myObject 实例化这个间隙 闯入这个if，导致“假单例”
                myObject = new MyObject();
            }
            return myObject;
        }
        
        
        private static MyObject dclUseMyObject;
        
        // DCL （Double Check Lock）两次检查锁 
        // 初期理解：
        // 1. 个人觉得更过浪费 ， "n*2*if"判断，"n"重的执行准备工作
        // 2. 代码结构更复杂，没有`synchronized`清晰明了
        // 3. `synchronized` 并不意味着性能低下， 既然 if == null判断代价不大，那么返回的速度
        
        // 我上面的理解好像完全没有问题，那为什么“这玩意”还被写进了书呢？
        
        // 后来的理解：
        // 1. 考虑的场景不同，初期的理解 是建立在 “高并发初始化”上的，如果真实的业务场景是这样的(绝大多数不会是这样)，以上的逻辑么有问题
        // 2. “高并发初始化”不是常见场景，“高并发取实例”才是普遍的场景，所以这样大多数线程都只会经过第一个if判断，然后直接返回，并且这个方法不是同步的，可以允许多个线程进入
        
        public static MyObject getDCLObjInstance() {  // 方法 无synchronized  (心生慰藉优化，多个线程可以进入这个方法)
            if (dclUseMyObject == null) { // CK1
                sleep(1000); // 模拟 实例化对象前的耗时准备工作 ------ (多个线程都会执行，既然是单例，这些工作是否每个线程都有必要做一遍呢？ -- 浪费1：重复执行准备工作 )
                
                synchronized (MyObject.class) { //同步类 (与其他static synchronized方法互斥、synchronized(MyObject.class)块互斥)
                    // 多个线程会依次计入这个代码块(通过了上一层if判断的线程)
                    if (dclUseMyObject == null) { // CK2 (再次判断 -- 浪费2：if 判断也是工作啊，不为空则返回之前创建的对象，更加证明上面的“准备工作”是多余的)
                        dclUseMyObject = new MyObject();
                    }
                }
            }
            return dclUseMyObject;
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
