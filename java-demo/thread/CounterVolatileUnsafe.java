import java.util.ArrayList;
import java.util.List;

public class CounterVolatileUnsafe {

    public static void main(String[] args) {
        Service service = new Service();
        
        List<Thread> threadList = new ArrayList<Thread>();
        for(int i = 0; i < 100; i++) {
            threadList.add(new Thread(() -> {
                service.increment();
            }));
        }
        threadList.forEach((t) -> {
            t.start();
        });

    }

    static class Service {
        volatile public static int count; // 静态共享变量，不管是单实例多线程、多实例多线程操作都需要考虑 线程安全问题

        public void increment() {
            for (int i = 0; i < 100; i++) {
                count++; // 不管是 count++、++count、count += 1 都是非原子操作的，都有三步：取值、计算、写值
            }
            System.out.println("count=" + count);
        }
    }
}
