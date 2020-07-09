package org.orh.rocketmq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rocksdb.*;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * delay_msg --->  broker(topic-dmq)----> dmq_consumer  ----(到了时间)--->  broker(topic-biz） ---> biz_consumer
 * <pre>
 * 1、通过修改 Producer 端，实际投递到 RocketMQ 的 topic 不是这个，而是替换为了一个统一的 topic，名为 dmq_inner_topic，原始 topic 被转为 body 的一部分。
 * 2、Rock-DMQ 项目会消费 dmq_inner_topic 这个特殊的 topic
 * 3、消费 dmq_inner_topic 的消息后，Rock-DMQ 项目会将其写入到本地的 RocksDB 中，key 为到期时间为前缀（这一点比较重要）
 * 4、Rock-DMQ 项目采用文中第二部分的内容相似的实现方式，隔一段时间去轮询 RocksDB ，看有无到期的消息
 * 5、如果有到期消息，Rock-DMQ 项目将这个消息投递到 RocketMQ 中
 * 6、订阅了这个 topic 的原有消费端就可以消费到这条消息了
 *
 * 作者：挖坑的张师傅
 * 链接：https://juejin.im/post/5e8fac605188256bdf72b24d
 * 来源：掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *  </pre>
 *
 * @author ouronghui
 * @since 2020/7/7
 */
public class CustomDelayMessageTest {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(CustomDelayMessageTest.class);

    static DefaultMQProducer producer = new DefaultMQProducer("default_producer_group");

    static DefaultMQPushConsumer bizConsumer;
    /**
     * Delay MQ - 真实应用时，可以用独立项目比如 Rock-DMQ  来消费自定义的延迟消息；这里为了演示方便，就放在了一起 用一个 独立 的 consumer 来区分
     */
    static DefaultMQPushConsumer dmqConsumer;

    static final String DMQ_INNER_TOPIC = "dmq_inner_topic";

    /**
     * 通过延时消息来取消订单
     */
    static final String CANCEL_ORDER_TOPIC = "cancel_order";

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


    @BeforeClass
    public static void setup() throws MQClientException, RocksDBException {
        initRocksDB();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    seekMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);

        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        //  bizConsumer
        bizConsumer = new DefaultMQPushConsumer("default_biz_consumer_group");
        bizConsumer.setNamesrvAddr("localhost:9876");
        startBizConsumer();

        dmqConsumer = new DefaultMQPushConsumer("default_dmq_consumer_group");
        dmqConsumer.setNamesrvAddr("localhost:9876");
        startDMQConsumer();
    }

    private static void initRocksDB() throws RocksDBException {
        DBOptions options = new DBOptions();
        options.setCreateIfMissing(true)
                .setCreateMissingColumnFamilies(true);

        List<ColumnFamilyDescriptor> columnFamilyDescriptors = new ArrayList<>();
        columnFamilyDescriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY));
        columnFamilyDescriptors.add(new ColumnFamilyDescriptor("custom_dmq".getBytes()));
        List<ColumnFamilyHandle> columnFamilyHandles = new ArrayList<>();

        rocksDB = RocksDB.open(options, "c:/Temp/rocks/mq-delay-message", columnFamilyDescriptors, columnFamilyHandles);
        log.info("columnFamilyHandles size: {}", columnFamilyHandles.size());
        readOptions = new ReadOptions();
        writeOptions = new WriteOptions();
        writeOptions.setSync(true);
    }


    @Test
    public void testDelayMessage() throws InterruptedException, RemotingException, MQClientException, MQBrokerException, UnsupportedEncodingException {
        // 运行后，观察日志中 包含"bizConsumer consume message"行， 对比其中的  assert consumeTime 与  time
        TimeUnit.SECONDS.sleep(5);
        sendDelayMessage(CANCEL_ORDER_TOPIC, genMessage(1L), 1L);
        sendDelayMessage(CANCEL_ORDER_TOPIC, genMessage(2L), 2L);
        sendDelayMessage(CANCEL_ORDER_TOPIC, genMessage(3L), 3L);
        sendDelayMessage(CANCEL_ORDER_TOPIC, genMessage(5L), 5L);
        sendDelayMessage(CANCEL_ORDER_TOPIC, genMessage(10L), 10L);
        sendDelayMessage(CANCEL_ORDER_TOPIC, genMessage(30L), 30L);
        sendDelayMessage(CANCEL_ORDER_TOPIC, genMessage(60L), 60L);
        TimeUnit.SECONDS.sleep(5);
        sendDelayMessage(CANCEL_ORDER_TOPIC, genMessage(1L), 1L);
        TimeUnit.SECONDS.sleep(300);
    }

    private static String genMessage(long delaySeconds) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("delay %s s, send time: %s, assert consumeTime: %s", delaySeconds, dtf.format(now), dtf.format(now.plusSeconds(delaySeconds)));
    }

    private SendResult sendDelayMessage(String topic, String msg, long delaySeconds) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message(topic, (msg).getBytes());
        if (delaySeconds <= 0) {
            log.info("not a delay message, send message to biz topic: {}.", topic);
            return producer.send(message);
        }
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.consumeTimestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + delaySeconds;
        delayMessage.originMessage = message;
        byte[] delayMessageBody = JSON.toJSONString(delayMessage).getBytes();
        Message delayMQMessage = new Message(DMQ_INNER_TOPIC, delayMessageBody);
        log.info("send message to dmq topic.");
        return producer.send(delayMQMessage);
    }

    static class DelayMessage {
        public Long consumeTimestamp;
        public Message originMessage;
    }

    private static void startBizConsumer() throws MQClientException {
        bizConsumer.subscribe(CANCEL_ORDER_TOPIC, "*");
        bizConsumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                log.info("bizConsumer consume message={}, time={}", new String(msg.getBody()), dtf.format(LocalDateTime.now()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        bizConsumer.start();
    }

    private static void startDMQConsumer() throws MQClientException {
        dmqConsumer.subscribe(DMQ_INNER_TOPIC, "*");
        dmqConsumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    byte[] delayMessageBody = msg.getBody();
                    DelayMessage delayMessage = JSON.parseObject(new String(delayMessageBody), DelayMessage.class);

                    Instant instant = Instant.ofEpochSecond(delayMessage.consumeTimestamp);
                    LocalDateTime assertConsumeTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    Duration between = Duration.between(LocalDateTime.now(), assertConsumeTime);
                    if (between.getSeconds() < 1) {
                        log.info("剩余：{}s,赶紧直接发送到业务topic...", between.getSeconds());
                        producer.send(delayMessage.originMessage);
                        continue;
                    }
                    log.info("dmqConsumer consume message={}, time={}", new String(delayMessage.originMessage.getBody()), dtf.format(LocalDateTime.now()));
                    saveMessageToLocalRepository(delayMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        dmqConsumer.start();
    }

    private static RocksDB rocksDB;
    private static WriteOptions writeOptions;
    private static ReadOptions readOptions;
    private static Long lastSeekSecondsTime;

    private static void saveMessageToLocalRepository(DelayMessage delayMessage) throws RocksDBException {
        String key = genKey(delayMessage.consumeTimestamp);
        String valueString = JSON.toJSONString(delayMessage.originMessage);
        byte[] value = valueString.getBytes(Charset.forName(RemotingHelper.DEFAULT_CHARSET));
        log.info("insert message to rocksDB, key:{}, value:{}", key, valueString);
        rocksDB.put(writeOptions, key.getBytes(), value);
    }

    private static String genKey(long timestamp) {
        return String.format("%s:DMQ:%S", timestamp, UUID.randomUUID().toString().replace("-", ""));
    }

    static class Key {
        // key 的格式
        public static Pattern keyPattern = Pattern.compile("(?<time>\\d+?):DMQ:(?<uuid>\\w+)");

        private String keyStr;

        private long time;

        private String uuid;

        public static Key from(String keyStr) {
            Matcher matcher = keyPattern.matcher(keyStr);
            if (!matcher.matches()) {
                return null;
            }
            Key key = new Key();
            key.keyStr = keyStr;
            key.time = Long.parseLong(matcher.group("time"));
            key.uuid = matcher.group("uuid");
            return key;
        }
    }

    private static void seekMessage() throws InterruptedException {
        // 当前时间戳，到秒
        long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        RocksIterator iter = rocksDB.newIterator(readOptions);

        // 如果时钟回拨或者还没到处理时间片，睡眠一段时间
        if (lastSeekSecondsTime != null && lastSeekSecondsTime > now) {
            TimeUnit.MILLISECONDS.sleep(400);
            return;
        }
//        if (lastSeekSecondsTime == null) {
//            iter.seekToFirst();
//            log.info("seek to first");
//            lastSeekSecondsTime = now;
//        } else {
//            iter.seek(String.valueOf(lastSeekSecondsTime).getBytes());
//            log.info("seek prefix: {}, now: {}", lastSeekSecondsTime, now);
//        }
        iter.seekToFirst();
        lastSeekSecondsTime = now;
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
            log.info("match time: {}, send to biz topic", key.time);
            sendToBizTopic(key, iter.value());
            iter.next();
        }
        log.info("seek one round end.");
        ++lastSeekSecondsTime;
    }

    private static void sendToBizTopic(Key key, byte[] value) {
        CompletableFuture.runAsync(() -> {
            try {
                // todo 消息格式转换错误处理 - delete 掉不符合格式的消息
                Message message = JSON.parseObject(value, Message.class);
                log.info("send message to real biz topic: {}", message.getTopic());
                producer.send(message);
                rocksDB.delete(key.keyStr.getBytes());
            } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException | RocksDBException e) {
                e.printStackTrace();
            }
        });

    }
}
