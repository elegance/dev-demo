package org.orh.sigleton;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SigletonTest4 {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MyObject myObject = MyObject.getInstance();

        FileOutputStream fos = new FileOutputStream("D:/myObjectFile.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(myObject);
        oos.close();
        fos.close();
        System.out.println(myObject.hashCode());

        FileInputStream fis = new FileInputStream("D:/myObjectFile.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        MyObject myObject2 = (MyObject) ois.readObject(); // 这种方式生成 myObject2，
                                                          // 没有通过私有的构造函数，所以MyObject
                                                          // 出现了两个实例(去除MyObject中的readResolve方法测试)，readObject 内部会判断
                                                          // 对象内部是否有readResolve方法，如果有就会自动调用

        ois.close();
        fis.close();
        System.out.println(myObject2.hashCode());
    }

    static class MyObject implements Serializable {
        private static final long serialVersionUID = -4388219891848462014L;

        // 内部类方式
        private static class MyObjectHandler {
            private static MyObject myObject = new MyObject();
        }

        private MyObject() {
            System.out.println("构造函数执行开始  threadName=" + Thread.currentThread().getName() + " time=" + System.currentTimeMillis());
            sleep(2000);
            System.out.println("构造函数执行结束");
        }

        public static MyObject getInstance() {
            return MyObjectHandler.myObject;
        }

        protected Object readResolve() {
            System.out.println("调用了 readResolve方法.");
            return MyObjectHandler.myObject;
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
