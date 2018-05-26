package org.orh.basic.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Class.getResource 与 ClassLoader.getResource 以及 ClassLoader.getResources 的区别
 */
public class TestResouces {

    public static void main(String[] args) throws IOException {
        // 注意如果你是maven 工程记得配置 resource 资源的输出，否则 properties 将不会被输出
        // 获取 3个 properties 文件的绝对路径： 1.properties, subpackage/2.properties , ../3.properties

        // 非 / 开头为相对路径取资源
        System.out.println(TestResouces.class.getResource("1.properties"));
        System.out.println(TestResouces.class.getResource("subpackage/2.properties"));

        // 使用 / 开头，绝对路径取资源
        System.out.println(TestResouces.class.getResource("/org/orh/basic/3.properties"));

        classLoaderResource();

        classLoaderResources();
    }

    /**
     * getResource 当前类加载器以及父类加载器所在路径的资源文件，遇到第一个匹配的资源文件则返回
     * 1. 当前工程的 classpath
     * 2. 引入的 jar 包中也可能包含指定的文件
     * 返回的是当前工程的 资源文件
     */
    //
    public static void classLoaderResource() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println("classLoader:" + classLoader);

        // classLoader.getResource() 获取的是 classpath 根路径的绝对地址  --- 从结果上看，等于 class.getResource("/")
        System.out.println("classLoader.getResource(\"\"): " + classLoader.getResource(""));
        System.out.println("classLoader.getResource(\"org\"): " + classLoader.getResource("org"));

        //  classLoader.getResource 不能指定 / 开头， '/'表示Boot ClassLoader中的加载范围，这个类加载器是C++实现的，所以加载范围为null
        // 源码可以看出 class.getResource() 其实也是由 ClassLoader 实现的，当以 / 开头时 会被 resolve 掉
        System.out.println("classLoader.getResource(\"/\"): " + classLoader.getResource("/"));
    }

    /**
     * getResources 当前类加载器及父类加载器所在路径的资源文件，匹配的全部返回
     * 1. 当前工程的 classpath
     * 2. 引入的 jar 包中也可能包含指定的文件
     * 返回的是所有匹配的资源文件
     */
    public static void classLoaderResources() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = classLoader.getResources("org");
        while (urls.hasMoreElements()) {
            System.out.println(urls.nextElement());
        }
    }
}
