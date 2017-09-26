package org.orh.serializable.protobuf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.orh.serializable.protobuf.PBUser.User;
/**
 * 
 * protoc 工具可以从此下载： https://github.com/google/protobuf/releases ，寻找对应平台的压缩包，例如protoc-3.4.0-win32.zip，不区分32位64位
 */
public class ProtobufSerializableTest {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // 设置对象存储位置
        String dir = System.getProperty("java.io.tmpdir");
        String saveFile = dir + "\\user-proto.dat";
        System.out.println("write file:" + saveFile);

        // 构建对象数据
        User.Builder builder = User.newBuilder();
        builder.setName("poxi").setAge(18);

        User user = builder.build();
        System.out.println("serialize write:" + user.toString());

        // 对象序列化成字节数组
        byte[] userByte = user.toByteArray();

        // 写入文件-真实应用中可能不是写入文件，可能是通过网络框架进行传递
        try (FileOutputStream fos = new FileOutputStream(saveFile);) {
            fos.write(userByte);
        }

        // 从文件读取字节数组，反序列化
        try (FileInputStream fis = new FileInputStream(saveFile);) {
            User user2 = User.parseFrom(fis);
            System.out.println("deserialize read:" + user2.toString());
        }

    }
}
