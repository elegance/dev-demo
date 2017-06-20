package org.orh.lock.reentrantLock;

import java.util.Scanner;

public class TestQueue {
    public static void main(String[] args) throws InterruptedException {
        ProductQueue<String> strQueue = new ProductQueue<>();
        strQueue.put("001");
        strQueue.put("002");
        strQueue.put("003");
        strQueue.put("004");

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            try {
                String ipt = null;
                System.out.println("请输入（多个请用“：”分隔）：");
                do {
                    ipt = scanner.nextLine();
                    for (String s :ipt.split(":")) {
                        strQueue.put(s);
                    }
                    System.out.println("请输入（多个请用“：”分隔）：");
                } while(ipt != null);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        String rs = null;
        do {
            rs = strQueue.take();
            System.out.println(rs);
        } while (rs != null);
        
    }
}
