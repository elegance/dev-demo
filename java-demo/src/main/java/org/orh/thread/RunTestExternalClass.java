package org.orh.thread;

import org.orh.thread.ExternalClass.InnerClass;
import org.orh.thread.ExternalClass.InnerStaticClass;

public class RunTestExternalClass {
    public static void main(String[] args) {
        ExternalClass publicClass = new ExternalClass();
        publicClass.setUserName("a");
        publicClass.setPassword("aaa");

        System.out.println(publicClass.getUserName() + ":" + publicClass.getPassword());

        InnerClass innerClass = publicClass.new InnerClass(); // 这里需要 蛋壳实例'publicClass.new'
        innerClass.setAddress("a's address");
        innerClass.setAge("18");
        System.out.println(innerClass.getAddress() + ":" + innerClass.getAge());

        InnerStaticClass innerStaticClass = new InnerStaticClass(); // 这里不需要蛋壳，直接就可以new 了
        innerStaticClass.setAddress("熟鸡蛋，做卤蛋");
        innerStaticClass.setAge("30");
        System.out.println(innerStaticClass.getAddress() + ":" + innerClass.getAge());

    }
}
