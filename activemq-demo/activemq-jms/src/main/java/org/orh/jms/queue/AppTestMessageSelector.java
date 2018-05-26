package org.orh.jms.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AppTestMessageSelector {
    private static final String url = "tcp://localhost:61616";

    private static final String queueName = "queue-test";

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        // connection 需要 start
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);

        String[] selectors = {"selectorA", "selectorB"};

        // 生产消息
        MessageProducer producer = session.createProducer(destination);
        new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                // 随机设置 selector-A/selector-B
                String selectorValue = selectors[ThreadLocalRandom.current().nextInt(2)];
                String messageText = "message-" + i + "[" + selectorValue + "]";
                try {
                    Message message = session.createTextMessage(messageText);
                    message.setStringProperty("myProperty", selectorValue);
                    producer.send(message);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                System.out.println("发送消息：" + messageText);
            }
        }).start();
//        sendConnection.close();


        // 消费者A -- selector 标记需要在 创建时加上，如果有 consumer 没有设置 selector, 那么 它能接收这个队列中的所有消息
        // 需要 使用独立的 session - session 单线程
        Session sessionA = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumerA = sessionA.createConsumer(destination, "myProperty='selectorA' and 1=1");
//        MessageConsumer consumerA = session.createConsumer(destination, "myProperty='selectorA' and 1=1");
        consumerA.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("消费者A接消息：" + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        // 消费者B
        Session sessionB = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumerB = sessionB.createConsumer(destination, "myProperty='selectorB'");
        consumerB.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("消费者B接消息：" + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });


    }
}
