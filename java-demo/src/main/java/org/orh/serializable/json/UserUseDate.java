package org.orh.serializable.json;

import lombok.Data;

import java.util.Date;

/**
 * User
 *
 * @author ouronghui
 * @since 2024/1/12 16:49
 */
@Data
public class UserUseDate {
    protected String id;
    protected Date create_time;
    protected Date update_time;
    /**
     * 用户来源
     */
    private String source;

    /**
     * 统一用户ID
     */
    private String user_id;

    /**
     * 注销时间（当user_status是DISABLED的时候才有意义）
     */
    private Date disabled_time;


    /**
     * 激活时间（当user_status非INACTIVE的时候才有意义）
     */
    private Date activation_time;
}
