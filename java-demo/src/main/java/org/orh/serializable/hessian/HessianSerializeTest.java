package org.orh.serializable.hessian;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class HessianSerializeTest {
    public static void main(String[] args) throws IOException {
        // 设置对象存储位置
        String dir = System.getProperty("java.io.tmpdir");
        String saveFile = dir + "\\user-hessian.dat";
        System.out.println("write file:" + saveFile);

        // 对象数据
        User user = new User();
        user.setName("poxi");
        user.setAge(18);

        System.out.println("serialize write:" + user);

        // 序列化写入磁盘
        try (FileOutputStream fos = new FileOutputStream(saveFile)) {
            Hessian2Output ho = new Hessian2Output(fos);
            ho.writeObject(user);
            ho.flush(); // 此处要手动 flush.
        }

        // 反序列化
        try (FileInputStream fis = new FileInputStream(saveFile);) {
            Hessian2Input hi = new Hessian2Input(fis);
            User user2 = (User) hi.readObject();
            System.out.println("deserialize read:" + user2);
        }
    }

}
