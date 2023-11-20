package org.orh.basic;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * ParameterNameDiscover - 通过asm字节码工具获取参数名 （字节码 本地变量表  LocalVariableTable）
 * 三种方式：
 * <pre>
 *     1. 借助字节码工具
 *     2. 使用 -parameters  （java8  , idea: java Compiler, maven: maven-compiler-plugin compilerArgs）
 *     3. -g + javap ...
 * </pre>
 * https://blog.csdn.net/f641385712/article/details/99112603
 *
 * @author ouronghui
 * @since 2023/11/20 19:51
 */
public class ParameterNameDiscover {
    public static void main(String[] args) throws NoSuchMethodException {
        Method method = ParameterNameDiscover.class.getMethod("test1", String.class, Integer.class);
        MethodParameter nameParameter = new MethodParameter(method, 0);
        MethodParameter ageParameter = new MethodParameter(method, 1);

        // 打印输出：
        // 使用Parameter输出
        Parameter nameOriginParameter = nameParameter.getParameter();
        Parameter ageOriginParameter = ageParameter.getParameter();
        System.out.println("===================源生Parameter结果=====================");
        System.out.println(nameOriginParameter.getType() + "----" + nameOriginParameter.getName());
        System.out.println(ageOriginParameter.getType() + "----" + ageOriginParameter.getName());
        System.out.println("===================MethodParameter结果=====================");
        System.out.println(nameParameter.getParameterType() + "----" + nameParameter.getParameterName());
        System.out.println(ageParameter.getParameterType() + "----" + ageParameter.getParameterName());
        System.out.println("==============设置上ParameterNameDiscoverer后MethodParameter结果===============");
        ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        nameParameter.initParameterNameDiscovery(parameterNameDiscoverer);
        ageParameter.initParameterNameDiscovery(parameterNameDiscoverer);
        System.out.println(nameParameter.getParameterType() + "----" + nameParameter.getParameterName());
        System.out.println(ageParameter.getParameterType() + "----" + ageParameter.getParameterName());
    }

    public Object test1(String name, Integer age) {
        String value = name + "---" + age;
        System.out.println(value);
        return value;
    }


}
