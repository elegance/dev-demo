package org.orh.pattern.ch04.ext;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * cglib 动态代理，
 * 
 * <pre>
 * 1. 委托类不可以是final，否则无法生成代理对象，报错。
 * 2. 方法不可是final的，否则无效
 * </pre>
 *
 */
public class CglibDynamicProxy {

    public static void main(String[] args) {
        UserServiceCglib cglib = new UserServiceCglib();
        UserServiceImpl userService = (UserServiceImpl) cglib.getInstance(new UserServiceImpl());
        System.out.println(userService.getClass());
        userService.addUser();
        System.out.println("--------------分割线------------------");
        userService.editUser();

    }

    // 1. 业务类，没有实现接口
    static class UserServiceImpl {
        public void addUser() {
            System.out.println("增加一个用户。。。");
        }

        public void editUser() {
            System.out.println("编辑一个用户。。。");
        }
    }

    // 2. 代理类
    static class UserServiceCglib implements MethodInterceptor {
        private Object target;

        public Object getInstance(Object target) {
            this.target = target;
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(this.target.getClass());
            enhancer.setCallback(this);
            return enhancer.create();
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            Object result = null;
            System.out.println("代理类方法，进行了增强。。。");
            System.out.println("事务开始。。。");
            result = proxy.invokeSuper(obj, args);
            System.out.println("事务结束。。。");
            return result;
        }
    }
}
