package org.orh.spring.jdbc.model;

import lombok.Data;

import java.util.Map;

@Data
public class SourceField {
    // 只需要有一条为真
    private static final String MATCH_ANY = "MATCH_ANY";
    // 所有条目都为真
    private static final String MATCH_ALL = "MATCH_ALL";
    // 记录存在
    private static final String EXISTS = "EXISTS";

    private static final String BOOL_EXISTS = "BOOL_EXISTS";       // 目标字段 = 条目存在 ？
    private static final String BOOL_MATCH_ANY = "BOOL_MATCH_ANY"; // 目标字段 = 任意条目源字段为true ？
    private static final String BOOL_MATCH_ALL = "BOOL_MATCH_ALL"; // 目标字段 = 所有条目源字段为true ？
    private static final String NUMBER_COUNT = "NUMBER_COUNT";     // 目标字段 = 所有条目数量
    private static final String NUMBER_SUM = "NUMBER_SUM";         // 目标字段 = 所有条目源字段之和
    private static final String DATE_MAX = "DATE_MAX";         // 目标字段 = 所有条目源字段之最大
    private static final String DATE_MIN = "DATE_MIN";         // 目标字段 = 所有条目源字段之最小

    /**
     * 源字段
     */
    private String column;
    /**
     * 目标字段
     */
    private String target;
    /**
     * bool值判断条件(请使用targetType)
     */
    @Deprecated
    private String boolCondition;
    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 时间字段对应的类型，对应的格式
     */
    private String dateFormat="yyyy-MM-dd HH:mm:ss";
    /**
     * 字段值映射
     */
    private Map<String, String> valueMap;

    public boolean shouldMatchAny() {
        return BOOL_MATCH_ANY.equals(targetType) || MATCH_ANY.equals(boolCondition);
    }

    public boolean shouldMatchAll() {
        return BOOL_MATCH_ALL.equals(targetType) || MATCH_ALL.equals(boolCondition);
    }

    public boolean shouldExists() {
        return BOOL_EXISTS.equals(targetType) || EXISTS.equals(boolCondition);
    }

    public boolean shouldCount() {
        return NUMBER_COUNT.equals(targetType);
    }

    public boolean shouldSum() {
        return NUMBER_SUM.equals(targetType);
    }

    public boolean shouldMaxDate(){
        return DATE_MAX.equals(targetType);
    }

    public boolean shouldMinDate(){
        return DATE_MIN.equals(targetType);
    }
}