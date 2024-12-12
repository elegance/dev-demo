package org.orh.rabbitmq;

import com.rabbitmq.client.impl.CredentialsProvider;
import com.rabbitmq.client.impl.CredentialsRefreshService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.amqp.CachingConnectionFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.ConnectionFactoryCustomizer;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionFactoryBeanConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * RabbitMQConfig
 *
 * @author: orh
 * @Desc
 * @create: 2024/11/6
 */
@Configuration
@Slf4j
public class RabbitMQConfiguration {

    @Bean
    public RabbitProperties rabbitProperties() {
        RabbitProperties rabbitProperties = new RabbitProperties();
        rabbitProperties.setAddresses(
                RabbitConsts.host + ":" + RabbitConsts.port /*+ "," +
                        RabbitConsts.host + ":" + (RabbitConsts.port + 1) + "," +
                        RabbitConsts.host + ":" + (RabbitConsts.port + 2)*/
        );
        rabbitProperties.setVirtualHost("/");
        rabbitProperties.setUsername(RabbitConsts.user);
        rabbitProperties.setPassword(RabbitConsts.password);
        return rabbitProperties;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        //确认消息 Exchange已发送到队列
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("[RabbitMQ]消息未发送Exchange,cause={},data={}", cause, correlationData);
            }
        });

        //消息从Exchange路由到Queue失败的回调
        rabbitTemplate.setReturnsCallback(returnCallback -> {
            log.error("[RabbitMQ]消息未路由到Queue,ReturnedMessage={}", returnCallback);
        });

        return rabbitTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer(RabbitProperties properties,
                                                                                ResourceLoader resourceLoader, ObjectProvider<CredentialsProvider> credentialsProvider,
                                                                                ObjectProvider<CredentialsRefreshService> credentialsRefreshService) {
        RabbitConnectionFactoryBeanConfigurer configurer = new RabbitConnectionFactoryBeanConfigurer(resourceLoader,
                properties);
        configurer.setCredentialsProvider(credentialsProvider.getIfUnique());
        configurer.setCredentialsRefreshService(credentialsRefreshService.getIfUnique());
        return configurer;
    }

    @Bean
    @ConditionalOnMissingBean
    CachingConnectionFactoryConfigurer rabbitConnectionFactoryConfigurer(RabbitProperties rabbitProperties,
                                                                         ObjectProvider<ConnectionNameStrategy> connectionNameStrategy) {
        CachingConnectionFactoryConfigurer configurer = new CachingConnectionFactoryConfigurer(rabbitProperties);
        configurer.setConnectionNameStrategy(connectionNameStrategy.getIfUnique());
        return configurer;
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionFactory.class)
    CachingConnectionFactory rabbitConnectionFactory(
            RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer,
            CachingConnectionFactoryConfigurer rabbitCachingConnectionFactoryConfigurer,
            ObjectProvider<ConnectionFactoryCustomizer> connectionFactoryCustomizers) throws Exception {

        RabbitConnectionFactoryBean connectionFactoryBean = new RabbitConnectionFactoryBean();
        rabbitConnectionFactoryBeanConfigurer.configure(connectionFactoryBean);
        connectionFactoryBean.afterPropertiesSet();
        com.rabbitmq.client.ConnectionFactory connectionFactory = connectionFactoryBean.getObject();
        connectionFactoryCustomizers.orderedStream()
                .forEach((customizer) -> customizer.customize(connectionFactory));

        CachingConnectionFactory factory = new CachingConnectionFactory(connectionFactory);
        rabbitCachingConnectionFactoryConfigurer.configure(factory);

        return factory;
    }

    // 单消息监听器容器工厂配置
    @Bean(name = "simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory singleMessageListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(5);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(10);

        return factory;
    }


    //批量消息监听器容器工厂配置
    @Bean(name = "batchMessageListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory batchMessageListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setBatchListener(true);
        factory.setConsumerBatchEnabled(true);
        factory.setBatchSize(5);
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(50);

        return factory;
    }

}
