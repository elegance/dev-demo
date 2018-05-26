package org.orh.nio;

import java.nio.ByteBuffer;

/**
 * position 当前读写位置、capacity 最大容量、limit 实际上限
 *
 * flip: 两个动作 先limit置为position，然后position置为0，用作读写转换， 即转换后代表 [position...limit]间为有效数据
 * rewind: 将position 置为0， 可用于重读
 * clear: 将position置为0，并将 limit置为capacity位置，清除作用
 */
public class ByteBufferTest2 {
    public static void main(String[] args) {
        ByteBuffer buf = ByteBuffer.allocate(15); // 15个字节缓存区大小

        System.out.println("limit: " + buf.limit() + ", capacity: " + buf.capacity() + ", position: " + buf.position());

        for (int i = 0; i < 10; i++) {
            buf.put((byte) i); // 存入10个字节数据
        }
        System.out.println("limit: " + buf.limit() + ", capacity: " + buf.capacity() + ", position: " + buf.position());

        buf.flip(); // 读写转换，重置position 为 0，将limit位置放到 原position 位置
                    // 意义：写了的数据，去读只需要从0 读到 limit，载体是同一个 byteBuffer

        System.out.println("limit: " + buf.limit() + ", capacity: " + buf.capacity() + ", position: " + buf.position());

        for (int i = 0; i < 5; i++) {
            System.out.print(buf.get());
        }
        System.out.println();
        System.out.println("limit: " + buf.limit() + ", capacity: " + buf.capacity() + ", position: " + buf.position());
        buf.flip();
        System.out.println("limit: " + buf.limit() + ", capacity: " + buf.capacity() + ", position: " + buf.position());
    }
}
