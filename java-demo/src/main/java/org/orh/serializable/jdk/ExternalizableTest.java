package org.orh.serializable.jdk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ExternalizableTest {
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        // 设置对象存储位置
        String dir = System.getProperty("java.io.tmpdir");
        String saveFile = dir + "\\user2.dat";
        System.out.println("write file:" + saveFile);

        // 对象数据
        User2 user = new User2();
        user.setName("poxi");
        user.setAge(18);
        
        System.out.println("serialize write:" + user);

        // 序列化，将对象写到磁盘文件
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));) {
            oos.writeObject(user);
        }

        // 反序列化，从磁盘文件读取到内存
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));) {
            User2 user2 = (User2) ois.readObject();
            System.out.println("deserialize read:" + user2);
        }
    }
}
