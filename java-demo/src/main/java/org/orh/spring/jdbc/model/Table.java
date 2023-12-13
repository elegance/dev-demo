package org.orh.spring.jdbc.model;

import lombok.Data;

import java.util.List;

/**
 * 数据源订阅表配置信息
 */
@Data
public class Table {

    /**
     * 监听的表名
     */
    private String name;

    /**
     * 主键
     */
    private String primaryKey;

    /**
     * 监听的字段名
     */
    private List<Field> fieldList;
}
