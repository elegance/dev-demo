package org.orh.serializable.jdk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * jdk 自带的序列化，主要依赖ObjectOutputStream.writeObject，ObjectInputStream.readObject
 *
 */
public class SerializableNormal {
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        // 设置对象存储位置
        String dir = System.getProperty("java.io.tmpdir");
        String saveFile = dir + "\\user.dat";
        System.out.println("write file:" + saveFile);

        // 对象数据
        User user = new User();
        user.setName("poxi");
        user.setAge(18);

        System.out.println("serialize write:" + user);

        // 序列化：将对象输出值文件，通过 ObjectOutputStream
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));) {
            oos.writeObject(user);
        }

        // 反序列化：将文件从磁盘读取都内存中
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));) {
            User user2 = (User) ois.readObject();
            System.out.println("deserialize read:" + user2);
        }
    }
}
