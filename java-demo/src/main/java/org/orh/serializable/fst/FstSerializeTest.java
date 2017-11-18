package org.orh.serializable.fst;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.orh.serializable.kryo.User;

public class FstSerializeTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 设置对象存储位置
        String dir = System.getProperty("java.io.tmpdir");
        String saveFile = dir + "\\user-fst.dat";
        System.out.println("write file:" + saveFile);

        // 对象数据
        User user = new User();
        user.setName("poxi");
        user.setAge(18);

        System.out.println("serialize write:" + user);
        
        
        // 序列化到磁盘
        try(FSTObjectOutput fstoo = new FSTObjectOutput(new FileOutputStream(saveFile));) {
           fstoo.writeObject(user);
        }
        
        // 反序列化
        try (FSTObjectInput fstoi = new FSTObjectInput(new FileInputStream(saveFile));) {
            User user2 = (User) fstoi.readObject();
            System.out.println("deserialize read:" + user2);
        }
    }
}
