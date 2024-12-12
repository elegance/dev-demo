package org.orh.rabbitmq.ch04;

import org.orh.rabbitmq.RabbitMQConfiguration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * TestClusterDelayQueue
 *
 * @author: orh
 * @Desc
 * @create: 2024/11/6
 */
public class TestClusterDelayQueue {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RabbitMQConfiguration.class);
        // RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);
        RabbitAdmin admin = context.getBean(RabbitAdmin.class);


        Map<String, Object> queueArgs = new HashMap<>();
        queueArgs.put("x-queue-type", "quorum");
        queueArgs.put("x-queue-leader-locator", "balanced");

        for (int i = 0; i < 6; i++) {
            Queue delayedQueue = new Queue("queue.test.declare-" + i, true, false, false, queueArgs);
            admin.declareQueue(delayedQueue);
        }
    }
}
