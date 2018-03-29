package org.orh.proxy.cglib;

import org.orh.proxy.Hello;
import org.orh.proxy.HelloImpl;

public class Test {
    public static void main(String[] args) {
        CGlibProxy1 cGlibProxy1 = new CGlibProxy1();
        Hello hello = cGlibProxy1.getProxy(HelloImpl.class);

        hello.say("Frank");
    }
}
