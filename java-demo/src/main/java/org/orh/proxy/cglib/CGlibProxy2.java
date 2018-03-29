package org.orh.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGlibProxy2 implements MethodInterceptor {
    private static final CGlibProxy2 instance = new CGlibProxy2();

    private CGlibProxy2() {
    }

    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }

    public static CGlibProxy2 getInstance() {
        return instance;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("before");
        Object rs = proxy.invokeSuper(obj, args);
        System.out.println("after");
        return rs;

    }
}
