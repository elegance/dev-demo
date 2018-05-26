package org.orh.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy2 implements InvocationHandler {
    private Object object;

    public DynamicProxy2(Object object) {
        this.object = object;
    }

    /**
     * 内置 Proxy.newProxyInstance 避免导出都是
     */
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        System.out.println(proxy.getClass());
        Object rs = method.invoke(object, args);
        System.out.println("after");
        return rs;
    }
}
