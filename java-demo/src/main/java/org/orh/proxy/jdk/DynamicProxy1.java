package org.orh.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy1 implements InvocationHandler {

    private Object target;

    public DynamicProxy1(Object target) {
        this.target = target;
    }

    /**
     * @param proxy  代理实例
     * @param method 目标对象的方法
     * @param args   方法参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object result = method.invoke(target, args);
        System.out.println("after");
        return result;
    }
}
