package org.orh.thread;
import java.util.ArrayList;
import java.util.List;

public class TwoThreadTransData {

    public static void main(String[] args) {
        Service service = new Service();

        Thread threadA = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    service.add();
                    System.out.printf("添加了 %d 个元素\n", i + 1);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "thread-a");

        Thread threadB = new Thread(() -> {
            try {
                while (true) {
                    if (service.size() == 5) { //cpu轮循 检查 size 是否等于 5，浪费资源
                        System.out.println("==5 了，线程b要退出了");
                        throw new InterruptedException();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "thread-b");

        threadA.start();
        threadB.start();
    }

    static class Service {
        volatile private List<String> list = new ArrayList<String>(); // 注意此处添加 volatile 标识，不然
                                                                      // b线程取到的都是0

        public void add() {
            list.add("阿毛");
        }

        public int size() {
            return list.size();
        }
    }
}
