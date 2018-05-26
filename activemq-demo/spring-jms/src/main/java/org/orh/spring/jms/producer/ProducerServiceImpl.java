package org.orh.spring.jms.producer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class ProducerServiceImpl implements ProducerService {

    @Autowired
    JmsTemplate jmsTemplate;

//    @Resource(name = "queueDestination")
    @Resource(name = "topicDestination")
    Destination destination;

    @Override
    public void sendMessage(String message) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                System.out.println("发送消息：" + message);
                return session.createTextMessage(message);
            }
        });
    }
}
