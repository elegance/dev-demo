package org.orh.rocketmq;

/**
 * 常量
 *
 * @author orh
 */
public interface Constant {
    String hostIp = "192.168.101.223";

    String nameSrv = String.format("%s:9876", hostIp);

    String producerGid = "GID_producer_default";

    /**
     * TODO 注意创建对应的 消费者group GID_A
     */
    String consumerCidA = "CID_A";

    String consumerCidB = "CID_B";

    String topic = "test_topic";

    String tag = "test_tag";
}
