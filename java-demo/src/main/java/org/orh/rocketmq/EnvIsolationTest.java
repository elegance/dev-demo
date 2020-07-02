package org.orh.rocketmq;

import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 非生产环境共用一套 mq server， 同一个topic 下不同环境 的生产者发送到不同队列, 不同环境的消费者从不同队列消费消息 <br>
 * 参考：https://mp.weixin.qq.com/s?subscene=23&__biz=MzU3Njk0MTc3Ng==&mid=2247483769&idx=1&sn=965331e4238eb15b016b92997a7955ae&chksm=fd0d7ac6ca7af3d05e8be38af03683458300cdc25e7e1d2797c6f6cdd62e94c53536d6224e0d&scene=7&key=17b3559d7137fbadd4dbd4d27a7746f9f55ef04deffc9fb5069acc53f2f4088a5542ae57d4b307ab90a015823269ceb0cd3def8c41c1d3f1c837c8c641099a1c441b69e9ba721cc2bfac161e8740b6a6&ascene=0&uin=ODc5ODc0MDU%3D&devicetype=Windows+10+x64&version=62090070&lang=zh_CN&exportkey=AZcNPQctPQYNDqfqxgl4lDA%3D&pass_ticket=CRwuxAXjGdEKlvkrQCwXxYdsQo3xhafCRt4Ykh8t6BE%3D
 * <p>
 * <b>1. RocketMQ 的消息投递提供了 MessageQueueSelector 接口可以自定义消息队列选择器，指定消息要投递的 queue (对于我们的场景，这里简化处理，根据环境的编号直接映射 queue)</b><br>
 * <b>2. 对于消费端，RocketMQ 定义了 AllocateMessageQueueStrategy 策略接口，可以自己实现当前消费者可以消费哪些 queue 队列。</b>
 * </p>
 *
 * @author ouronghui
 * @since 2020/7/1
 */
public class EnvIsolationTest {
    static DefaultMQProducer producer = new DefaultMQProducer("default_producer_group");

    static HashMap<String, Integer> envQueueIndexMap;

    static MessageQueueSelector envBasedMessageQueueSelector;

    static DefaultMQPushConsumer devConsumer;
    static DefaultMQPushConsumer qaConsumer;
    static DefaultMQPushConsumer prodConsumer;

    @BeforeClass
    public static void setup() throws MQClientException {
        // env 与 队列号的映射关系
        envQueueIndexMap = new HashMap() {{
            put("dev", 0);
            put("qa", 2);
            // 如 perf, prod 环境完全独占的使用一套 mq server ，我们则不需要配置他们的映射关系，使用默认的策略，使用topic中所有的队列
        }};

        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        // ★ 根据 env 选择到队列的 选择器
        envBasedMessageQueueSelector = (messageQueueList, message, env) -> messageQueueList.get(envQueueIndexMap.get(env));

        // ★ consumer 设置
        devConsumer = new DefaultMQPushConsumer("default_consumer_group", null, new EnvAllocateMessageQueueStrategy("dev"), true, "");
        devConsumer.setNamesrvAddr("localhost:9876");
        devConsumer.setInstanceName("dev");

        qaConsumer = new DefaultMQPushConsumer("default_consumer_group", null, new EnvAllocateMessageQueueStrategy("qa"), true, "");
        qaConsumer.setNamesrvAddr("localhost:9876");
        qaConsumer.setInstanceName("qa");

        prodConsumer = new DefaultMQPushConsumer("default_consumer_group", null, new EnvAllocateMessageQueueStrategy("prod"), true, "");
        prodConsumer.setNamesrvAddr("localhost:9876");
        prodConsumer.setInstanceName("prod");
    }

    static class EnvAllocateMessageQueueStrategy implements AllocateMessageQueueStrategy {
        private String env;
        private AllocateMessageQueueStrategy defaultStrategy;

        public EnvAllocateMessageQueueStrategy(String env) {
            this.env = env;
            defaultStrategy = new AllocateMessageQueueAveragely();
        }

        @Override
        public List<MessageQueue> allocate(String consumerGroup, String currentCID, List<MessageQueue> list, List<String> cidAll) {
            Integer queueIndex = envQueueIndexMap.get(env);
            if (queueIndex == null) {
                return defaultStrategy.allocate(consumerGroup, currentCID, list, cidAll);
            }
            List<MessageQueue> matchList = new ArrayList<>();
            matchList.add(list.get(queueIndex));
            // System.out.printf("env=%s, matchList=%s\n", env, matchList);
            return matchList;
        }

        @Override
        public String getName() {
            Integer queueIndex = envQueueIndexMap.get(env);
            if (queueIndex == null) {
                return defaultStrategy.getName();
            }
            return "env-based";
        }
    }

    @Test
    public void envIsolationTest() throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String topic = "env_isolation_test";

        // 实际使用时 env 变量从自己的配置中读取 。判断出当前是 dev/qa/prod 等
        sendMessage(new Message(topic, "TagA", ("dev-msg1").getBytes(RemotingHelper.DEFAULT_CHARSET)), "dev");
        sendMessage(new Message(topic, "TagA", ("dev-msg2").getBytes(RemotingHelper.DEFAULT_CHARSET)), "dev");
        sendMessage(new Message(topic, "TagA", ("qa-msg1").getBytes(RemotingHelper.DEFAULT_CHARSET)), "qa");
        sendMessage(new Message(topic, "TagA", ("qa-msg2").getBytes(RemotingHelper.DEFAULT_CHARSET)), "qa");

        // 使用countDownLatch 来感知消息消费完成
        CountDownLatch countDownLatch = new CountDownLatch(4);

        // 消费消息验证 （在此方法中进行断言）
        startConsumers(countDownLatch, topic, devConsumer, qaConsumer);
        shutdownConsumers(devConsumer, qaConsumer);
        countDownLatch.await();
    }

    @Test
    public void sendProdMsg() throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        sendMessage(new Message("prod-topic", "TagA", ("prod-msg1").getBytes(RemotingHelper.DEFAULT_CHARSET)), "prod");
        sendMessage(new Message("prod-topic", "TagA", ("prod-msg2").getBytes(RemotingHelper.DEFAULT_CHARSET)), "prod");
        sendMessage(new Message("prod-topic", "TagA", ("prod-msg3").getBytes(RemotingHelper.DEFAULT_CHARSET)), "prod");
        sendMessage(new Message("prod-topic", "TagA", ("prod-msg4").getBytes(RemotingHelper.DEFAULT_CHARSET)), "prod");
        CountDownLatch countDownLatch = new CountDownLatch(4);
        startConsumers(countDownLatch, "prod-topic", prodConsumer);
        countDownLatch.await();
        shutdownConsumers(prodConsumer);
    }


    private void startConsumers(CountDownLatch countDownLatch, String topic, DefaultMQPushConsumer... consumers) throws MQClientException {
        for (DefaultMQPushConsumer consumer : consumers) {
            consumer.subscribe(topic, "*");
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                for (MessageExt msg : msgs) {
                    try {
                        int queueId = context.getMessageQueue().getQueueId();
                        String instanceName = consumer.getInstanceName();
                        String env = instanceName;
                        String message = new String(msg.getBody());
                        Integer envIndex = envQueueIndexMap.get(env);

                        // prod 由于没有配置映射，可以消费所有队列的消息
                        if (envIndex != null) {
                            // 断言 queueId 等于 我们map 配置的 queueIndex
                            assertThat(queueId, equalTo(envIndex));
                            // 断言 消息内容 是以我们上面 设定的 "env" 开头的， 即环境隔离的
                            assertThat(message, startsWith(env));
                        }
                        System.out.printf("thread=%s, consumer instance=%s, consume msg=%s, messageQueueId=%s, msgId=%s\n", Thread.currentThread().getName(), instanceName, message, queueId, msg.getMsgId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            System.out.printf("consumer: %s started..\n", consumer.getInstanceName());
            consumer.start();
        }
    }

    private void shutdownConsumers(DefaultMQPushConsumer... consumers) {
        for (DefaultMQPushConsumer consumer : consumers) {
            consumer.shutdown();
        }
    }

    private SendResult sendMessage(Message message, String env) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Integer queueIdx = envQueueIndexMap.get(env);
        if (queueIdx == null) {
            return producer.send(message);
        }
        // ★ 发送消息时 使用 队列选择器
        return producer.send(message, envBasedMessageQueueSelector, env);
    }
}
