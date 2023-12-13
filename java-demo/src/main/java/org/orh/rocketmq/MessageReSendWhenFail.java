package org.orh.rocketmq;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.lang.WeightRandom.WeightObj;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.RandomUtil;
import org.rocksdb.*;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mq 消息发送失败，存储至 rocksDB ,然后定时从rocksDB取数重试
 *
 * @author ouronghui
 * @since 2020/6/18
 */
public class MessageReSendWhenFail {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(MessageReSendWhenFail.class);

    /**
     * 最后查询 rocksDB 的时间戳， 单位为秒
     */
    private Long lastSeekSecondsTime;

    private RocksDB rocksDB;

    private ColumnFamilyHandle mqRetryCFHandler;

    private ReadOptions readOptions;

    private static WriteOptions writeOptions;

    private static int[] RETRY_TIME_STEP_ARRAY = new int[]{
            3, 5, 30, 60, 120, 300, 480, 600, 900, 1800
    };

    private ThreadPoolExecutor reconsumeThreadPool;

    private void insert(byte[] msg, int retryCount, String typeName) throws RocksDBException {
        String key = genKey(retryCount, typeName);
        log.info("insert : key={}, value={}", key, new String(msg));
        rocksDB.put(mqRetryCFHandler, writeOptions, key.getBytes(), msg);
    }

    public static void main(String[] args) throws InterruptedException, RocksDBException, IOException {
        if (args.length > 0) {
            // 客户端使用 socket 连接来插入测试数据
            startTestSocketClient();
            return;
        }
        // 服务端来 监听 rocksdb 以及启动一个 serverSocket 供客户端操作
        MessageReSendWhenFail reConsumer = new MessageReSendWhenFail();
        reConsumer.init();

        reConsumer.startServerSocket();
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * key => expireTime:RETRY_COUNT:typeName:uuid
     *
     * @param retryCount 当前重试次数
     * @param typeName   类型名称
     */
    private static String genKey(int retryCount, String typeName) {
        int retryAfter = retryCount < RETRY_TIME_STEP_ARRAY.length ? RETRY_TIME_STEP_ARRAY[retryCount] : RETRY_TIME_STEP_ARRAY[RETRY_TIME_STEP_ARRAY.length - 1];
        long expire = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + retryAfter;
        return String.format("%s:%s:%s:%s", expire, retryCount, typeName, UUID.randomUUID().toString().replace("-", ""));
    }

    static class Key {
        // key 的格式
        public static Pattern keyPattern = Pattern.compile("(?<time>\\d+?):(?<retryCount>\\d+?):(?<type>\\w+?):(?<uuid>\\w+)");

        private String keyStr;

        private long time;

        private int retryCount;

        private String type;

        private String uuid;

        public static Key from(String keyStr) {
            Matcher matcher = keyPattern.matcher(keyStr);
            if (!matcher.matches()) {
                return null;
            }
            Key key = new Key();
            key.keyStr = keyStr;
            key.time = Long.parseLong(matcher.group("time"));
            key.retryCount = Integer.parseInt(matcher.group("retryCount"));
            key.type = matcher.group("type");
            key.uuid = matcher.group("uuid");
            return key;
        }
    }

    private void init() throws RocksDBException {
        Options options = new Options();
        options.setCreateIfMissing(true)
                .setCompressionType(CompressionType.NO_COMPRESSION);

        rocksDB = RocksDB.open(options, "c:/Temp/rocks/mq-resend");
        readOptions = new ReadOptions();
        mqRetryCFHandler = rocksDB.getDefaultColumnFamily();

        writeOptions = new WriteOptions();
        writeOptions.setSync(true);

        ThreadFactory threadFactory = ThreadFactoryBuilder.create().setNamePrefix("resend-mq-msg-").build();
        reconsumeThreadPool = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    private void loop() throws InterruptedException {
        // 当前时间戳，到秒
        long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        RocksIterator iter = rocksDB.newIterator(mqRetryCFHandler, readOptions);

        // 如果时钟回拨或者还没到处理时间片，睡眠一段时间
        if (lastSeekSecondsTime != null && lastSeekSecondsTime > now) {
            TimeUnit.MILLISECONDS.sleep(400);
            return;
        }
        if (lastSeekSecondsTime == null) {
            iter.seekToFirst();
            log.info("seek to first");
            lastSeekSecondsTime = now;
        } else {
            iter.seek(String.valueOf(lastSeekSecondsTime).getBytes());
            log.info("seek prefix: {}, now: {}", lastSeekSecondsTime, now);
        }
        while (iter.isValid()) {
            String keyStr = new String(iter.key(), StandardCharsets.UTF_8);
            Key key = Key.from(keyStr);
            if (key == null) {
                log.info("ignore malformed key: {}", keyStr);
                continue;
            }
            // 也可以通过设置 readOptions IterateUpperBound 来减少下面这个判断，不过有这个判断可以打印下最近将要重新消费的key
            if (key.time > lastSeekSecondsTime) {
                log.info("[{}] key's time is greater than the currently seek time [{}]", keyStr, lastSeekSecondsTime);
                break;
            }
            log.info("match time: {}, do reSend", key.time);
            reSend(key, iter.value());
            iter.next();
        }
        log.info("seek one round end.");
        ++lastSeekSecondsTime;
    }

    private void reSend(Key key, byte[] message) {
        reconsumeThreadPool.execute(() -> {
            try {
                MsgHandler handler = getHandler(key.type);
                if (handler == null) {
                    return;
                }
                boolean isSuccess = handler.handle(message);
                log.info("handle result: {}, key: {}", isSuccess, key.keyStr);
                // 如果不成功，则重新写入 RocksDB
                String messageStr = new String(message);
                if (!isSuccess) {
                    int currentRetryCount = key.retryCount + 1;
                    int maxRetryCount = MsgHandler.RETRY_COUNT;
                    if (currentRetryCount >= RETRY_TIME_STEP_ARRAY.length || currentRetryCount >= maxRetryCount) {
                        log.info("send reach limit, retry count:{}, default count:{}, custom count:{}, msg: {}",
                                currentRetryCount, RETRY_TIME_STEP_ARRAY.length, maxRetryCount, messageStr);
                        // exceptionHandle.handler("retry $currentRetryCount fail, msg:$msgString");
                        return;
                    }
                    log.info("process failed , reinsert ： {}", messageStr);
                    insert(message, currentRetryCount, key.type);
                    rocksDB.delete(key.keyStr.getBytes());
                    return;
                }
                log.info("process success , delete ： key={}, value={}", key.keyStr, messageStr);
                rocksDB.delete(key.keyStr.getBytes());
            } catch (Exception e) {
                log.error("An exception occurred during re-consumption:", e);
                // exceptionHandle.handler("retry $currentRetryCount fail, msg:$msgString");
            }
        });
    }

    interface MsgHandler {
        int RETRY_COUNT = 10;

        boolean handle(byte[] msg);
    }

    static final WeightRandom<Boolean> wr;

    static {
        // 模拟 99.99% true 处理成功， 0.01 false 处理失败
        WeightObj<Boolean>[] weightObjs = new WeightObj[]{new WeightObj(Boolean.TRUE, 99.99), new WeightObj(Boolean.FALSE, 0.01)};
        wr = RandomUtil.weightRandom(weightObjs);
    }

    private MsgHandler getHandler(String typeName) {
        log.info("type: {} , getHandler.", typeName);

        return msg -> {
            log.info("handle msg: {}", new String(msg));
            try {
                Thread.sleep(RandomUtil.randomInt(5, 50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return wr.next();
        };
    }

    /**
     * 服务端开启 socket ，供客户端连接来写数据到 rocksDB
     * rocksDB 同一份数据，只能一个写连接打开
     */
    private void startServerSocket() {
        CompletableFuture.runAsync(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(6666);
                while (true) {
                    Socket client = serverSocket.accept();
                    CompletableFuture.runAsync(() -> {
                        try {
                            InputStream inputStream = client.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                log.info("line: {}", line);
                                String[] fields = line.split(",");
                                insert(fields[0].getBytes(), Integer.parseInt(fields[1]), "refund");
                            }
                        } catch (Exception ignore) {
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 客户端发送模拟数据
     *
     * @throws IOException
     */
    private static void startTestSocketClient() throws IOException {
        Socket socket = new Socket("127.0.0.1", 6666);
        for (int i = 0; i < 1000; i++) {
            int retryNumber = RandomUtil.randomInt(0, 3);
            String msg = "test-msg-" + i;
            int retryAfter = RETRY_TIME_STEP_ARRAY[retryNumber];
            long expire = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + retryAfter;
            log.info(msg + " expire :" + expire);
            socket.getOutputStream().write((msg + "," + retryNumber + "\r\n").getBytes());
        }
    }
}
