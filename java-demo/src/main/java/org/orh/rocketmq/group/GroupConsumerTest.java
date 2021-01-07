package org.orh.rocketmq.group;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.orh.rocketmq.Constant;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ouronghui
 * @since 2021/1/7
 */
@Slf4j
public class GroupConsumerTest {

    static DefaultMQPushConsumer consumer;

    public static void init(String[] args) {
        String gid = args[0];

        String instanceName = args[1];
        consumer = new DefaultMQPushConsumer("1".equals(gid) ? Constant.consumerCidB : Constant.consumerCidA);
        consumer.setNamesrvAddr(Constant.nameSrv);
        consumer.setInstanceName(instanceName);
        consumer.setClientIP(Constant.hostIp);
    }

    /**
     * 传入不同的 args，比如依次传入：0 client1,  0 client2,  0 client3, 0 client4, 0 client5, 1 client6 分别启动，可以验证以下：
     * <ol>
     * <li>相同group ，队列消费</li>
     * <li>相同group ，消费者多于 queue 数量时，有消consumer始终处于闲消，费不到消息</li>
     * <li>相同group ，关闭正在消费消息的consumer, 空闲consumer 进而自动消费消息</li>
     * <li>writeQueueNums, readQueueNums 缩容、扩容， 空闲消费数量的变化</li>
     * </ol>
     * <p></p>
     * <ol>
     * <li>不同group ，广播消费</li>
     * </ol>
     *
     * @param args
     * @throws MQClientException
     */
    public static void main(String[] args) throws MQClientException {
        init(args);
        DefaultListener defaultListener = new DefaultListener(consumer, Stream.of(
                new SubscribeTopicAndTags(Constant.topic, Constant.tag)
        ).collect(Collectors.toList()));
        defaultListener.subscribeAndStart();
        while (true) {
            System.out.printf("输入：\n 1. exit: 退出 \n 2. 其他任意: 继续监听\n");
            String line = new Scanner(System.in).nextLine();
            if ("exit".equals(line)) {
                break;
            }
        }
        consumer.shutdown();
    }

    @Data
    static class DefaultListener implements MessageListenerConcurrently {

        public DefaultListener(DefaultMQPushConsumer consumer, List<SubscribeTopicAndTags> subscribeTopicAndTags) {
            this.consumer = consumer;
            this.subscribeTopicAndTags = subscribeTopicAndTags;
        }

        public void subscribeAndStart() throws MQClientException {
            for (SubscribeTopicAndTags subscribeTopicAndTag : subscribeTopicAndTags) {
                String subExpression = subscribeTopicAndTag.getTags().stream().collect(Collectors.joining("||"));
                log.info("consumer[group={}, instance={}], subscribe: topic={}, subExpression={}", consumer.getConsumerGroup(), consumer.getInstanceName(),
                        subscribeTopicAndTag.getTopic(), subExpression);
                consumer.subscribe(subscribeTopicAndTag.getTopic(), subExpression);
            }
            consumer.registerMessageListener(this);
            consumer.start();
        }

        private DefaultMQPushConsumer consumer;

        private List<SubscribeTopicAndTags> subscribeTopicAndTags;

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            for (MessageExt msg : msgs) {
                try {
                    int queueId = context.getMessageQueue().getQueueId();
                    String instanceName = consumer.getInstanceName();
                    String message = new String(msg.getBody());
                    String group = consumer.getConsumerGroup();
                    log.info("thread={}, consumer instance={}, consume msg={}, messageQueueId={}, msgId={}, group={}",
                            Thread.currentThread().getName(), instanceName, message, queueId, msg.getMsgId(), group);
                } catch (Exception e) {
                    log.error("err:", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

//        /**
//         * 消费消息
//         * @param msg 泛型的消息
//         * @param originalMessage
//         */
//        abstract void doConsumeMessage(T msg, MessageExt originalMessage);

        // public abstract List<SubscribeTopicAndTags> getSubscribeTopicAndTags();
    }

    @Data
    @NoArgsConstructor
    static class SubscribeTopicAndTags {
        public SubscribeTopicAndTags(String topic, String... tags) {
            this.topic = topic;
            this.tags = Arrays.stream(tags).collect(Collectors.toSet());
        }

        private String topic;
        private Set<String> tags;
    }
}
