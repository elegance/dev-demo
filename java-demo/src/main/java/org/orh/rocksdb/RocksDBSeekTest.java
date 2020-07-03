package org.orh.rocksdb;

import org.junit.BeforeClass;
import org.junit.Test;
import org.rocksdb.*;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * seek 测试
 *
 * @author ouronghui
 * @since 2020/6/30
 */
public class RocksDBSeekTest {

    static RocksDB rocksDB;

    static ColumnFamilyHandle columnFamilyHandler;

    static WriteOptions writeOptions;

    static final String[] testKeys = {"10", "11", "12", "22", "21", "20", "c", "b", "a", "A", "B", "C", "0"};

    @BeforeClass
    public static void setup() throws RocksDBException {
        Options options = new Options();
        options
                .setCreateIfMissing(true)
                .setCompressionType(CompressionType.NO_COMPRESSION);
        rocksDB = RocksDB.open(options, "c:/Temp/rocks/seek-test");
        // columnFamilyHandler 列族， 类似于 表， 这里使用的默认"表"
        columnFamilyHandler = rocksDB.getDefaultColumnFamily();
        writeOptions = new WriteOptions();
        writeOptions.setSync(true);

        rocksDB.deleteRange("0".getBytes(), "z".getBytes());

        // 插入测试数据
        for (int i = 0; i < testKeys.length; i++) {
            rocksDB.put(columnFamilyHandler, writeOptions, testKeys[i].getBytes(), "none".getBytes());
        }
    }

    /**
     * 按顺序输出所有数据
     */
    @Test
    public void testSeekFirst() {
        RocksIterator iter = rocksDB.newIterator(columnFamilyHandler);
        iter.seekToFirst();
        int idx = 0;
        String[] assertPrintKeys = {"0", "10", "11", "12", "20", "21", "22", "A", "B", "C", "a", "b", "c"};

        while (iter.isValid()) {
            String key = new String(iter.key(), StandardCharsets.UTF_8);
            System.out.println(key);
            assertThat(key, equalTo(assertPrintKeys[idx]));
            idx++;
            iter.next();
        }
    }

    @Test
    public void testSeek() {
        RocksIterator iter = rocksDB.newIterator(columnFamilyHandler);
        // seek 从 21 开始，20 是否会被打印呢？  上面是按 [22, 21, 20] 顺序插入的
        // 如果 数据是按插入时的数据来遍历的， 那么会被打印， 如果是按key 排序的，那么它不会被打印
        iter.seek("21".getBytes());
        int cnt = 0;
        while (iter.isValid()) {
            String key = new String(iter.key(), StandardCharsets.UTF_8);
            System.out.println(key);
            // 断言 20 永远不会被打印， 即数据按照顺序输出
            assertThat(key, not(equalTo("20")));
            cnt++;
            iter.next();
        }
    }

    @Test
    public void testIterateUpperBound() {
        // 目标数据：{"0", "10", "11", "12", "20", "21", "22", "A", "B", "C", "a", "b", "c"};
        ReadOptions readOptions = new ReadOptions();
        // 22 作为上界， 不包含22 ,即结果为6个数："0", "10", "11", "12", "20", "21"
        readOptions.setIterateUpperBound(new Slice("22"));

        RocksIterator iter = rocksDB.newIterator(columnFamilyHandler, readOptions);
        iter.seekToFirst();
        int cnt = 0;
        while (iter.isValid()) {
            String key = new String(iter.key(), StandardCharsets.UTF_8);
            System.out.println(key);
            assertThat(key, not(equalTo("22")));
            iter.next();
            cnt++;
        }
        assertThat(cnt, equalTo(6));

        // 上界设置为0 ，则不会遍历到数据
        readOptions.setIterateUpperBound(new Slice("0"));
        iter = rocksDB.newIterator(columnFamilyHandler, readOptions);
        iter.seekToFirst();
        assertThat(iter.isValid(), equalTo(false));
    }
}
