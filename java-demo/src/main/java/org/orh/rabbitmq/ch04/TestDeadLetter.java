package org.orh.rabbitmq.ch04;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.orh.rabbitmq.RabbitMQConfiguration;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * TestDeadLetter
 *
 * @author: orh
 * @Desc
 * @create: 2024/11/6
 */
@Slf4j
public class TestDeadLetter {

    public static final String EXCHANGE_NORMAL = "exchange.normal.video";
    public static final String EXCHANGE_DEAD_LETTER = "exchange.dead.letter.video";

    public static final String ROUTING_KEY_NORMAL = "routing.key.normal.video";
    public static final String ROUTING_KEY_DEAD_LETTER = "routing.key.dead.letter.video";

    public static final String QUEUE_NORMAL = "queue.normal.video";
    public static final String QUEUE_DEAD_LETTER = "queue.dead.letter.video";

    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RabbitMQConfiguration.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        SimpleRabbitListenerContainerFactory containerFactory = context.getBean("simpleRabbitListenerContainerFactory", SimpleRabbitListenerContainerFactory.class);

        SimpleMessageListenerContainer listenerContainer = containerFactory.createListenerContainer();
        rejectNormalQueueMsg(listenerContainer);

        SimpleMessageListenerContainer deadContainer = containerFactory.createListenerContainer();
        receiveDeadQueueMsg(deadContainer);

        testSendMessageButReject(rabbitTemplate);

        testSendMultiMessage(rabbitTemplate);
    }

    private static void receiveDeadQueueMsg(SimpleMessageListenerContainer deadContainer) {

        deadContainer.setQueueNames(QUEUE_DEAD_LETTER);
        deadContainer.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            // 监听死信队列
            log.info("★[dead letter] 我是死信监听方法，我接收到了死信消息 dataString = " + new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        });
        deadContainer.start();
    }

    private static void rejectNormalQueueMsg(SimpleMessageListenerContainer listenerContainer) {
        listenerContainer.setQueueNames(QUEUE_NORMAL);
        listenerContainer.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            // 监听正常队列，但是拒绝消息
            log.info("★[normal]消息接收到，但我拒绝。" + new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        });
        listenerContainer.start();
    }


    public static void testSendMessageButReject(RabbitTemplate rabbitTemplate) {
        rabbitTemplate
                .convertAndSend(
                        EXCHANGE_NORMAL,
                        ROUTING_KEY_NORMAL,
                        "测试死信情况1：消息被拒绝");
        log.info("send over...");
    }

    public static void testSendMultiMessage(RabbitTemplate rabbitTemplate) {
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(
                    EXCHANGE_NORMAL,
                    ROUTING_KEY_NORMAL,
                    "测试死信情况2：消息数量超过队列的最大容量" + i);
        }
    }
}
