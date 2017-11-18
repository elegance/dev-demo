package org.orh.serializable.kryo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializeTest {
    public static void main(String[] args) throws KryoException, FileNotFoundException {
        // 设置对象存储位置
        String dir = System.getProperty("java.io.tmpdir");
        String saveFile = dir + "\\user-kryo.dat";
        System.out.println("write file:" + saveFile);

        // 对象数据
        User user = new User();
        user.setName("poxi");
        user.setAge(18);

        System.out.println("serialize write:" + user);

        Kryo kryo = new Kryo();

        // 序列化，写到磁盘
        try (Output output = new Output(new FileOutputStream(saveFile));) {
            kryo.writeObject(output, user);
        }

        // 反序列化，从磁盘读到内存
        try (Input input = new Input(new FileInputStream(saveFile));) {
            User user2 = kryo.readObject(input, User.class);
            System.out.println("deserialize read:" + user2);
        }
    }
}
