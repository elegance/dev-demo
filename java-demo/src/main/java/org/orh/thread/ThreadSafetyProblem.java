package org.orh.thread;
public class ThreadSafetyProblem {

    static class HasSelfPrivateNum {
        private int num;

        public void addI(String userName) {
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
        HasSelfPrivateNum numRef = new HasSelfPrivateNum();

        Thread t1 = new Thread(() -> {
            numRef.addI("a");
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            numRef.addI("b");
        });
        t2.start();
    }
}
