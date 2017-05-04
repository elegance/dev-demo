public class TwoStop {

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();

        Thread threadA = new Thread(() -> {
            service.methodA();
        }, "thread-A");

        Thread threadB = new Thread(() -> {
            service.methodB();
        }, "thread-B");

        threadA.start(); // methodA 一直在循环，未释放锁
        threadB.start(); // methodB 一直处于挂起等待的堵塞状态
        
        while(true) {
            System.out.printf("%s: %s\n", threadA.getName(), threadA.getState());
            System.out.printf("%s: %s\n", threadB.getName(), threadB.getState());
            Thread.sleep(1000);
        }
    }

    static class Service {
        synchronized public void methodA() {
            System.out.println("methodA begin");
            boolean isContinueRun = true; //方法内部变量，外部无法改变，这里只是为了演示本节所要表达的内容
            while (isContinueRun) {
            }
            System.out.println("methodA end");
        }
        
        synchronized public void methodB() {
            System.out.println("methodB begin");
            System.out.println("methodB end");
        }
    }
}
