package org.orh.rocketmq.select.broker;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.impl.producer.TopicPublishInfo;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ProducerSendSelectBrokerTest
 * <pre>
 *  1. 发送时会根据当前topic查找路由信息，org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl#tryToFindTopicPublishInfo(java.lang.String)
 *  2. 得到的路由信息，也就是队列数据，会根据队列的 读、写权限过滤一份 可发送的队列数据
 *  3. 如果有多个 broker, 一个topic 设置的16个对列，那么两个broker 会有 2*16=32 队列可供选择，如果其中有不可读写的队列将会排除掉
 *  4. 选择队列发送：org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl#selectOneMessageQueue(org.apache.rocketmq.client.impl.producer.TopicPublishInfo, java.lang.String)
 *
 * </pre>
 *
 * @author ouronghui
 * @since 2023/12/6 16:24
 */
public class ProducerSendSelectBrokerTest {
    static DefaultMQProducer producer = new DefaultMQProducer("canal-server");

    @SneakyThrows
    public static void main(String[] args) {
//        producer.setNamesrvAddr("10.1.14.71:9876;10.1.14.72:9876;10.1.14.73:9876");
        producer.setNamesrvAddr("10.1.9.87:9876");
        producer.start();

        for (int i = 0; i < 32; i++) {
            SendResult result = producer.send(new Message("canal_sync_mtc_cms_cms_center", "tags", RandomUtil.randomInt(1, 99999) + "", ("{}").getBytes(Charset.forName("UTF-8"))));
            System.out.println(result.getMessageQueue().getBrokerName());
        }


      /*  ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 0, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        // topic, 队列的路由信息，
        String queueListStr = "[{\"brokerName\":\"broker-a\",\"queueId\":0,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":1,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":2,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":3,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":4,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":5,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":6,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":7,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":8,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":9,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":10,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":11,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":12,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":13,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":14,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-a\",\"queueId\":15,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":0,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":1,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":2,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":3,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":4,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":5,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":6,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":7,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":8,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":9,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":10,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":11,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":12,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":13,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":14,\"topic\":\"canal_sync_mtc_cms_cms_center\"},{\"brokerName\":\"broker-d\",\"queueId\":15,\"topic\":\"canal_sync_mtc_cms_cms_center\"}]";
        List<MessageQueue> messageQueueList = JSON.parseArray(queueListStr, MessageQueue.class);

        TopicPublishInfo topicPublishInfo = new TopicPublishInfo();
        // topicPublishInfo.getSendWhichQueue().getAndIncrement()
        CountDownLatch latch = new CountDownLatch(100);

        topicPublishInfo.setMessageQueueList(messageQueueList);
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                MessageQueue mqSelected = topicPublishInfo.selectOneMessageQueue();
                System.out.println(mqSelected.getBrokerName() + "     " + mqSelected.getQueueId());
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();*/
    }
}
