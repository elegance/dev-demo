package org.orh.nio;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * ByteBuffer ，参考自 ： [图解bytebuffer](https://my.oschina.net/talenttan/blog/889887)
 *
 */
public class ByteBufferTest {
    public static void main(String[] args) {
        // byte[] bytes: 用来存储数据
        // int capacity : 容量初始，不可变 => = bytes.size();
        // int limit: 实际装了多少数据 => limit <= capacity
        // int position: 用来表示在哪个位置开始往 bytes写数据或是读数据，此值是灵活变动的  => 0 <= position < capacity

        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put((byte) 3);

        byteBuffer.position(0); // 设置position到0位置，这样读数据就从这个位置开始
        byteBuffer.limit(1); // 设置 limit为1，表示byteBuffer 的有效数据长度是1

        byte b = byteBuffer.get();
        System.out.println(b);

        System.out.println(byteBuffer);

        byte[] data = byteBuffer.array();
        System.out.println(Arrays.toString(data));
    }

}
