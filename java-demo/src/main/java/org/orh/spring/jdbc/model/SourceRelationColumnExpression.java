package org.orh.spring.jdbc.model;

import lombok.Data;

@Data
public class SourceRelationColumnExpression {
    /**
     * 当前表字段
     */
    private String column;
    /**
     * 关联表字段
     */
    private String relationColumn;

}