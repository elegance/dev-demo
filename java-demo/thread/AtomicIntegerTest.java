import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
    public static void main(String[] args) {
        Service service = new Service();

        Thread t1 = new Thread(() -> {
            service.accumulatorAdd();
        });
        Thread t2 = new Thread(() -> {
            service.accumulatorAdd();
        });
        Thread t3 = new Thread(() -> {
            service.accumulatorAdd();
        });
        Thread t4 = new Thread(() -> {
            service.accumulatorAdd();
        });
        Thread t5 = new Thread(() -> {
            service.accumulatorAdd();
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }

    static class Service {
        private AtomicInteger count = new AtomicInteger(0);

        public void accumulatorAdd() {
            for (int i = 0; i < 10000; i++) {
                System.out.println(count.incrementAndGet());
            }
        }
    }
}
