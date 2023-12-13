package org.orh.spring.jdbc.model;

import lombok.Data;

/**
 * 数据源订阅字段配置
 *
 */
@Data
public class Field {

    /**
     * 字段名
     */
    private String name;

    /**
     * 在业务接口VO中映射的字段名
     */
    private String bizName;
}
