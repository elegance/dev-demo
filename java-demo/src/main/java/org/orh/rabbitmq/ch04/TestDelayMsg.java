package org.orh.rabbitmq.ch04;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.orh.rabbitmq.RabbitMQConfiguration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TestDelayMsg
 *
 * @author: orh
 * @Desc
 * @create: 2024/11/6
 */
@Slf4j
public class TestDelayMsg {
    static String EXCHANGE_DELAY = "exchange.delay.happy",
            QUEUE_DELAY = "queue.delay.video",
            ROUTING_KEY_DELAY = "routing.key.delay.happy";

    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RabbitMQConfiguration.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        SimpleRabbitListenerContainerFactory containerFactory = context.getBean("simpleRabbitListenerContainerFactory", SimpleRabbitListenerContainerFactory.class);


        declareExchangeQueueBinding(context);

        testSendDelayMessage(rabbitTemplate);

        SimpleMessageListenerContainer listenerContainer = containerFactory.createListenerContainer();
        listenerContainer.setQueueNames(QUEUE_DELAY);
        listenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                log.info("★[delay]消息接收到，我马上处理。" + new String(message.getBody(), StandardCharsets.UTF_8));
            }
        });
        listenerContainer.start();
    }

    private static void declareExchangeQueueBinding(AnnotationConfigApplicationContext context) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("x-delayed-type", "direct");
        CustomExchange delayedExchange = new CustomExchange(EXCHANGE_DELAY, "x-delayed-message", true, false, maps);
        RabbitAdmin admin = context.getBean(RabbitAdmin.class);
        admin.declareExchange(delayedExchange);

        Queue delayedQueue = new Queue(QUEUE_DELAY, true, false, false);
        admin.declareQueue(delayedQueue);

        Binding delayedBinding = BindingBuilder.bind(delayedQueue).to(delayedExchange).with(ROUTING_KEY_DELAY).noargs();
        admin.declareBinding(delayedBinding);
    }

    public static void testSendDelayMessage(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.convertAndSend(
                EXCHANGE_DELAY,
                ROUTING_KEY_DELAY,
                "测试基于插件的延迟消息 [" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "]",
                messageProcessor -> {

                    // 设置延迟时间：以毫秒为单位
                    messageProcessor.getMessageProperties().setHeader("x-delay", "10000");

                    return messageProcessor;
                });
    }
}
