package org.orh.proxy.fixed;

import org.orh.proxy.Hello;

public class HelloProxy implements Hello {

    private Hello hello;

    public HelloProxy(Hello hello) {
        this.hello = hello;
    }

    @Override
    public void say(String name) {
        System.out.println("before...");
        hello.say(name);
        System.out.println("after...");
    }
}
