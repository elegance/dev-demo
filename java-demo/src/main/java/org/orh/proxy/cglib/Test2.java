package org.orh.proxy.cglib;

import org.orh.proxy.Hello;
import org.orh.proxy.HelloImpl;

public class Test2 {
    public static void main(String[] args) {
        Hello helloProxy = CGlibProxy2.getInstance().getProxy(HelloImpl.class);
        helloProxy.say("Lily");
    }
}
