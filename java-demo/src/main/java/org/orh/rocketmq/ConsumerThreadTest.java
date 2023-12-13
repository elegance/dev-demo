package org.orh.rocketmq;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ConsumerThreadTest
 * <pre>
 *     1. 每个consumer 会创建线程池，验证 每个consumer 的线程池是不共享的 =================> 每个consumer 的线程池是独立的； 彼此间的线程名成会重复
 *     org.apache.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl#start 创建线程池
 * 	        => org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService#ConsumeMessageConcurrentlyService
 *     2. 测试动态线程，是否可以达到预期， org.apache.rocketmq.client.consumer.DefaultMQPushConsumer#updateCorePoolSize （先进线程池队列？）
 *     3. 一个consumer 订阅多个topic, 如果一个topic中的消息过多，导致线程使用满， 是否会阻塞另一个topic 中的消息消费 （新发送的到非阻塞的topic 消息是否能被即时消费）
 *     4. test setConsumeMessageBatchMaxSize
 *     5. org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService#consumeRequestQueue 不会被撑爆炸么？
 *          a: org.apache.rocketmq.client.consumer.DefaultMQPushConsumer#pullThresholdSizeForQueue 控制, DefaultMQPushConsumerImpl#pullMessage(org.apache.rocketmq.client.impl.consumer.PullRequest) 会有判断
 *          cachedMessageCount > this.defaultMQPushConsumer.getPullThresholdForQueue()
 *          cachedMessageSizeInMiB > this.defaultMQPushConsumer.getPullThresholdSizeForQueue()
 *           long cachedMessageCount = processQueue.getMsgCount().get();  -- 得到消息条数
 *           long cachedMessageSizeInMiB = processQueue.getMsgSize().get() / (1024 * 1024); -- 得到消息size
 *     6. DefaultMQPushConsumer.setConsumeThreadMax 与 core 不一致时， 就算再多消息也不会启用 max
 *
 * </pre>
 *
 * @author ouronghui
 * @since 2023/10/30 12:01
 */
@Slf4j
public class ConsumerThreadTest {

    private static String nameSrv = "10.1.9.87:9876";

    @SneakyThrows
    @Test
    public void genMsg() {
        String topic = "test0098";

        DefaultMQProducer producer = new DefaultMQProducer(Constant.producerGid);
        producer.setNamesrvAddr(nameSrv);
        producer.start();
        for (int i = 0; i < 1001; i++) {
            SendResult result = producer.send(new Message(topic, (" a message " + i).getBytes(Charset.forName("UTF-8"))));
        }
        System.out.println("send over.");
        sleep(1000 * 1000);
    }

    @SneakyThrows
    public static void main(String[] args) {
        DefaultMQPushConsumer workerConsumer1 = new DefaultMQPushConsumer("group-test-1");
        workerConsumer1.setNamesrvAddr(nameSrv);
        workerConsumer1.subscribe("test0098", "*");
        workerConsumer1.subscribe("test0099", "*");
        workerConsumer1.setConsumeThreadMin(5); // 后期可通过  updateCorePoolSize 动态调整
         workerConsumer1.setConsumeThreadMax(200); // 后期修改无效，
        workerConsumer1.setPullThresholdSizeForQueue(100);  // 大小100Mi
        workerConsumer1.setPullThresholdForQueue(10);  // 条数
        // workerConsumer1.setConsumeFromWhere();
        // workerConsumer1.registerMessageListener();
        workerConsumer1.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                process(msgs, context);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        workerConsumer1.start();


        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.print("输入：\n 0. 退出 \n 2. 修改消费线程数量\n");
            String line = new Scanner(System.in).nextLine();
            if (!NumberUtils.isNumber(line)) {
                return;
            }
            int n = Integer.parseInt(line);
            workerConsumer1.setConsumeThreadMax(n);
            workerConsumer1.updateCorePoolSize(n - 1);
            log.info("max: " + workerConsumer1.getConsumeThreadMax());
            log.info("min:" + workerConsumer1.getConsumeThreadMin());

//            workerConsumer1.getDefaultMQPushConsumerImpl().getConsumeMessageService()
        }, 1000L, 1000L, TimeUnit.MILLISECONDS);
        TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
    }


    @SneakyThrows
    private static void sleep(long millis) {
        TimeUnit.MILLISECONDS.sleep(millis);
    }

    private static void process(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        log.info(msgs.toString());
        sleep(10 * 1000);
    }
}
