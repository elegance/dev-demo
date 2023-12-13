package org.orh.spring.jdbc.model;

import lombok.Data;

import java.util.List;

@Data
public class SourceRelation {

    private static final String RELATION_CONDITION_AND = "AND";

    private static final String RELATION_CONDITION_OR = "OR";

    /**
     * 数据源
     */
    private String database;
    /**
     * 数据表
     */
    private String table;
    /**
     * 依赖的序号
     */
    private int number = 0;
    /**
     * 多条件相互关系(AND/OR)
     */
    private String relationCondition = RELATION_CONDITION_AND;
    /**
     * 关联条件
     */
    private List<SourceRelationColumnExpression> columnExpressions;
    /**
     * 是否索引方向，表示该关联关系是否指向索引主体的方向，用于沿该方向找出索引主体
     */
    private boolean direction;

    public boolean relationIsAnd() {
        return RELATION_CONDITION_AND.equals(relationCondition);
    }

    public boolean relationIsOr() {
        return RELATION_CONDITION_OR.equals(relationCondition);
    }

    public String getSourceKey() {
        return database + '-' + table + '-' + number;
    }

}