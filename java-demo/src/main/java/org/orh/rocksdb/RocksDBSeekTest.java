package org.orh.rocksdb;

import org.junit.BeforeClass;
import org.junit.Test;
import org.rocksdb.*;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * seek 测试
 * @author ouronghui
 * @since 2020/6/30
 */
public class RocksDBSeekTest {

    static RocksDB rocksDB;

    static ColumnFamilyHandle columnFamilyHandler;

    static WriteOptions WRITE_OPTIONS_SYNC;

    static final String[] testKeys = {"10", "11", "12", "22", "21", "20", "c", "b", "a", "A", "B", "C", "0"};

    @BeforeClass
    public static void setup() throws RocksDBException {
        org.rocksdb.Options options = new org.rocksdb.Options();
        options.setCreateIfMissing(true);
        rocksDB = RocksDB.open(options, "c:/Temp/rocks/seek-test");
        // columnFamilyHandler 列族， 类似于 表， 这里使用的默认"表"
        columnFamilyHandler = rocksDB.getDefaultColumnFamily();
        WRITE_OPTIONS_SYNC = new WriteOptions();
        WRITE_OPTIONS_SYNC.setSync(true);

        rocksDB.deleteRange("0".getBytes(), "z".getBytes());

        // 插入测试数据
        for (int i = 0; i < testKeys.length; i++) {
            rocksDB.put(columnFamilyHandler, WRITE_OPTIONS_SYNC, testKeys[i].getBytes(), "none".getBytes());
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

}
