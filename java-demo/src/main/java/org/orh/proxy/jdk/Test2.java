package org.orh.proxy.jdk;

import org.orh.proxy.Hello;
import org.orh.proxy.HelloImpl;

public class Test2 {
    public static void main(String[] args) {
        Hello hello = new HelloImpl();

        DynamicProxy2 dynamicProxy2 = new DynamicProxy2(hello);
        Hello helloProxy = dynamicProxy2.getProxy();
        helloProxy.say("Eric");
    }
}
