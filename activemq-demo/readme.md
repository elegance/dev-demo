#### 环境搭建(docker 安装)

```sh
docker run -d -p 8161:8161 -p 61616:61616 -e ACTIVEMQ_ADMIN_LOGIN=admin -e ACTIVEMQ_ADMIN_PASSWORD=admin --name activemq webcenter/activemq
```

#### 管理界面

http://localhost:8161/

#### JMS 编码步骤
1. 生产者
```java
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

// 7. 创建消息
TextMessage textMessage = session.createTextMessage("test-" + i);

// 8. 发送消息
producer.send(textMessage);

// 9. 关闭连接
connection.close();
```


#### JMS Selector
1. 关于选择器的格式, 即支持 =, != , like , +, - 等等，所以在**定义一般Selector时 不要使用特殊字符**

#### 建立Connection, Session
1. 一个 Connection 可以多个 Session
2. Session 单线程串行