package org.orh.proxy.jdk;

import org.orh.proxy.Hello;
import org.orh.proxy.HelloImpl;

import java.lang.reflect.Proxy;

public class Test1 {
    public static void main(String[] args) {
        Hello hello = new HelloImpl();
        DynamicProxy1 invocationHandler = new DynamicProxy1(hello);

        // 通过 Proxy.newProxyInstance 生成代理类
        // 参数：Classloader, 该实现类的接口， invocationHandler
        Hello helloProxy = (Hello) Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), invocationHandler);
        helloProxy.say("Bob");
    }
}
