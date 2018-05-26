package org.orh.ratelimiter.impl;

import org.orh.ratelimiter.AbstractRateLimiter;

/**
 * @author orh
 * @description TODO 使用 Redis 实现，主要思路同MemoryRateLimiter, redis 键值结构 ：标志符(要限流的方法、用户等) -> list(长度为要限定次数)
 * @date 2018/4/4 9:58
 */
public class RedisRateLimiter extends AbstractRateLimiter {

    private static final String LIMITER_PREFIX = "limiter";

    /**
     * 构造一个限定时间窗口内只能访问指定次数的限定器，比如 1分钟内只允许调用 10次， 即 new RedisRateLimiter(60, 10)
     *
     * @param timeWindow 事件窗口，单位为 秒
     * @param limitCount 限定次数
     */
    public RedisRateLimiter(long timeWindow, int limitCount) {
        super(timeWindow, limitCount);
    }

    @Override
    public boolean canInvoke(String invokeSignature) {
        throw new RuntimeException("Not yet supported!");
    }

    public String getSignature(String appid, String method, String userId) {
        return LIMITER_PREFIX + ":" + appid + ":" + method + ":" + userId;
    }
}
