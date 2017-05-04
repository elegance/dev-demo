public class TwoObjectTwoLock {

    static class HasSelfPrivateNum {
        private int num;

        synchronized public void addI(String userName) {
            try {
                if ("a".equals(userName)) {
                    num = 100;
                    System.out.println("a set over!");
                    Thread.sleep(1000);
                } else {
                    num = 200;
                }
                System.out.printf("%s num=%d\n", userName, num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        HasSelfPrivateNum numRef1 = new HasSelfPrivateNum(); // 实例1 内部有 synchronized addI方法
        HasSelfPrivateNum numRef2 = new HasSelfPrivateNum(); // 实例2 内部有 synchronized addI方法

        Thread t1 = new Thread(() -> {
            numRef1.addI("a");
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            numRef2.addI("b");
        });
        t2.start();
    }
}
