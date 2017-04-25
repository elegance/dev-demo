package pattern.ch06;

import java.io.UnsupportedEncodingException;

/**
 * 深度拷贝的实现
 * 
 * <pre>
 * 1. 类实现Cloneable接口，重写clone方法，在clone方法内将引用的对象也进行clone一份。（引用的对象同样也需要满足前面的条件 实现Cloneable接口，重写clone方法）
 * 2. 可以使用序列化反序列化的方式来实现 深度拷贝。 （单例模式中，编写readResolve 方法防止反序列化出现多个实例）
 * </pre>
 *
 */
public class DeepClone {
    public static void main(String[] args) throws CloneNotSupportedException, UnsupportedEncodingException {
         Body body = new Body(new Head(new Face()));
         Body body1 = (Body) body.clone();
         System.out.println(body == body1); // false // clone 对象输出false，表明不是复制的引用
         System.out.println(body.head == body1.head); // false 嵌套对象输出，false 表明为深度拷贝
         System.out.println(body.head.face == body1.head.face); // true 表明这里不符合深度拷贝, 将Face
        // 中的注释的clone方法替换掉现有的clone方法，完成深度拷贝

        // 深度拷贝条件：
        // 如果想要深度拷贝一个对象，这个对象要实现Cloneble接口，实现clone方法，并且在
        // clone方法内要把该对象的所有的其他引用对象也要clone一份，即要求被引用的对象满足深度拷贝条件（实现Cloneable接口，实现clone方法）
    }
    
    static class Body implements Cloneable {
        public Head head;

        public Body() {}

        public Body(Head head) {
            this.head = head;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Body newBody = (Body) super.clone();
            newBody.head = (Head) head.clone();
            return newBody;
        }
    }
    static class Head implements Cloneable {
        public Face face;

        public Head() {}

        public Head(Face face) {
            this.face = face;
        }

        // @Override
        // protected Object clone() throws CloneNotSupportedException {
        // Head newHead = (Head) super.clone();
        // newHead.face = (Face) face.clone();
        // return newHead;
        // }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
    static class Face implements Cloneable {
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
