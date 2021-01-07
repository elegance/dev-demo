package org.orh.rocketmq.group;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.orh.rocketmq.Constant;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author orh
 */
@Slf4j
public class MsgProducer {

    static DefaultMQProducer producer = new DefaultMQProducer(Constant.producerGid);

    private static void init() throws MQClientException {
        producer.setNamesrvAddr(Constant.nameSrv);
        producer.start();
    }

    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {
        init();
        while (true) {
            sendMsg(Constant.topic, Constant.tag, 4);
            System.out.printf("输入：\n 1. exit: 退出发送端 \n 2. 其他任意: 重新发送消息\n");
            String line = new Scanner(System.in).nextLine();
            if ("exit".equals(line)) {
                break;
            }
        }
        producer.shutdown();
    }

    private static void sendMsg(String topic, String tags, int num) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        for (int i = 0; i < num; i++) {
            SendResult result = producer.send(new Message(topic, tags, tags, ("a tag message " + i).getBytes(Charset.forName("UTF-8"))));
            log.info("topic={}, tag={}, send result: {}", topic, tags, result.getMsgId());
        }
    }
}
