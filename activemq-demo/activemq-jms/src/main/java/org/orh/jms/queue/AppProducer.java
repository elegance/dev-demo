package org.orh.jms.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AppProducer {

    private static final String url = "tcp://localhost:61616";

    private static final String queueName = "queue-test";

    public static void main(String[] args) throws JMSException {

        // 1. 创建 ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        // 2. 创建 Connection
        Connection connection = connectionFactory.createConnection();

        // 3. 启动连接
        connection.start();

        // 4. 创建会话 (事务模式，应答模式)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 5. 创建一个目标
        Destination destination = session.createQueue(queueName);

        // 6. 创建生产者
        MessageProducer producer = session.createProducer(destination);

        for (int i = 1; i <= 100; i++) {
            // 7. 创建消息
            TextMessage textMessage = session.createTextMessage("test-" + i);

            // 8. 发送消息
            producer.send(textMessage);

            System.out.println("发送消息：" + textMessage.getText());
        }

        // 9. 关闭连接
        connection.close();


    }
}
