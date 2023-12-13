package org.orh.spring.jdbc.model;

import lombok.Data;

import java.util.List;


@Data
public class Source {
    /**
     * 所属 syncId
     */
    private String syncId;
    /**
     * 数据源
     */
    private String database;
    /**
     * 数据表
     */
    private String table;
    /**
     * 序号，有些表在同一配置会出现多次，序号用于区分
     */
    private int number = 0;
    /**
     * 是否索引主体
     */
    private boolean index;
    /**
     * 是否被监听
     */
    private boolean subscribed;
    /**
     * 该表是否，该表除了bool/sum/count外，其他字段均为数组
     * 注：如果该值为false，即使最终查处的结果有多条，仅保留最后一条
     */
    private boolean multiLine;
    /**
     * 逻辑删除标识列，0表示未删除，其他表示删除
     */
    private String deleteColumn;
    /**
     * 表示时间的列，用于修复最近数据 - 注意在db上对此列添加索引
     */
    private String timeColumn;
    /**
     * sql hints
     */
    private String timeColumnQueryHints;
    /**
     * 额外条件
     */
    private String extraConditions;
    /**
     * 字段转换映射，【约定第一条为主键】
     */
    private List<SourceField> fields;
    /**
     * 数据表关联信息
     */
    private List<SourceRelation> relations;

    public String getIndexColumn() {
        return fields.get(0).getColumn();
    }

    public String getKey() {
        return database + '-' + table + '-' + number;
    }

}