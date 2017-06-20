package org.orh.thread;
public class SuspendResumeNoSameValue {

    static class User {
        private String userName = "1";
        private String password = "11";

        public void SetValue(String u, String p) {
            this.userName = u;

            if (Thread.currentThread().getName().equals("a")) {
                System.out.println("暂停a线程");
                Thread.currentThread().suspend();
            }
            this.password = p;
        }

        public void PrintUserInfo() {
            System.out.printf("%s %s", this.userName, this.password);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final User user = new User();
        
        Thread t1 = new Thread(() -> {
            user.SetValue("a", "aa");
        });
        t1.setName("a");
        t1.start();
        Thread.sleep(5000);
        
        Thread t2 = new Thread(() -> {
            user.PrintUserInfo();
        });
        t2.start();
    }
}
