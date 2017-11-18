package org.orh.pattern.ch04.ext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk 动态代理:  jdk 动态生成一个动态代理类，生成响应字节码，然后通过ClassLoader 加载字节码
 */
public class JdkDynamicProxy {
    
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        ServiceInvocationHandler handler = new ServiceInvocationHandler(userService);
        
        // 根据目标生成代理对象
        UserService proxy = (UserService) handler.getProxy();
        System.out.println(proxy.getClass());
        proxy.addUser();
        System.out.println("--------------分割线------------------");
        proxy.editUser();
    }

    // 1. 业务接口
    static interface UserService {

        // 增加一个用户
        public void addUser();

        // 编辑一个用户
        public void editUser();
    }

    // 2. 业务实现类
    static class UserServiceImpl implements UserService {
        public void addUser() {
            System.out.println("增加一个用户。。。");
        }

        public void editUser() {
            System.out.println("编辑一个用户。。。");
        }
    }

    // 3. 代理类 (动态通用)
    static class ServiceInvocationHandler implements InvocationHandler {
        // 目标对象（委托类）
        private Object target;

        public ServiceInvocationHandler(Object target) {
            super();
            this.target = target;
        }

        // 创建代理实例，暴露给外部使用，取得动态代理实例
        public Object getProxy() {
            // jdk 动态生成一个动态代理类，生成响应字节码，然后通过ClassLoader 加载字节码
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), this.target.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            System.out.println("代理类方法，进行了增强。。。");
            System.out.println("事务开始。。。");
            result = method.invoke(target, args);
            System.out.println("事务结束。。。");
            return result;
        }

    }
}
