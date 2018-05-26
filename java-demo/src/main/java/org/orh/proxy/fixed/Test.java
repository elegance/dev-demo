package org.orh.proxy.fixed;

import org.orh.proxy.HelloImpl;

public class Test {
    public static void main(String[] args) {
        HelloProxy helloProxy = new HelloProxy(new HelloImpl());
        helloProxy.say("Jim");
    }
}
