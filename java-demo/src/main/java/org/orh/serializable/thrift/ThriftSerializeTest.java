package org.orh.serializable.thrift;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

/**
 * java 类生成，参考：http://thrift.apache.org/tutorial/java
 * 
 * .thrif 格式请参考：https://git-wip-us.apache.org/repos/asf?p=thrift.git;a=blob_plain;f=tutorial/tutorial.thrift
 *
 */
public class ThriftSerializeTest {
    public static void main(String[] args) throws IOException, TException {
        // 设置对象存储位置
        String dir = System.getProperty("java.io.tmpdir");
        String saveFile = dir + "\\user-thrift.dat";
        System.out.println("write file:" + saveFile);

        // 对象数据
        User user = new User();
        user.setName("poxi");
        user.setAge(18);

        System.out.println("serialize write:" + user);

        // 序列化 成字节数组
        TSerializer serializer = new TSerializer();
        byte[] userByte = serializer.serialize(user);

        // 写入到文件
        try (FileOutputStream fos = new FileOutputStream(saveFile);) {
            fos.write(userByte);
        }

        TDeserializer deserializer = new TDeserializer();

        // 从文件读取
        try (FileInputStream fis = new FileInputStream(saveFile);) {
            User user2 = new User();
            deserializer.deserialize(user2, toByteArray(fis)); // 反序列化
            System.out.println("deserialize read:" + user2);
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int n;
        while (-1 != (n = input.read(buffer))) {
            baos.write(buffer, 0, n);
        }
        return baos.toByteArray();
    }
}
