package org.orh.thread;
import java.util.concurrent.TimeUnit;

public class SyncBlockString {

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();

        Thread thread1 = new Thread(() -> {
            service.setUserNamePassword("a", "aaa");
        }, "thread1");

        Thread thread2 = new Thread(() -> {
            service.setUserNamePassword("b", "bbb");
        }, "thread2");

        thread1.start();
        thread2.start();

        while (true) {
            System.out.println(service);
            if (!thread1.isAlive() && !thread2.isAlive()) {
                break;
            }
            Thread.sleep(800);
        }
    }

    static class Service {
        private String userName;
        private String password;
        private String anyString = new String();

        @Override
        public String toString() { //toString 访问userName、password是非同步的，这里是为了演示输出
            String flag = "✔";
            if (userName != null) {
                if (password == null || !password.contains(userName)) {
                    flag = "✘";
                }
            }
            return "Service [userName=" + userName + ", password=" + password + "] -----" + flag;
        }

        public void setUserNamePassword(String userName, String password) {
            try {
                String threadName = Thread.currentThread().getName();

                synchronized (anyString) {
                    System.out.printf("线程名称为：%s 在 %d 进入同步块\n", threadName, System.currentTimeMillis());
                    TimeUnit.SECONDS.sleep(1);
                    this.userName = userName;
                    TimeUnit.SECONDS.sleep(2);
                    this.password = password;
                    System.out.printf("线程名称为：%s 在 %d 离开同步块\n", threadName, System.currentTimeMillis());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
