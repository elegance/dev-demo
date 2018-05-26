package org.orh.ratelimiter;

/**
 * @author orh
 * @description
 * @date 2018/4/4 10:36
 */
public abstract class AbstractRateLimiter implements RateLimiter {

    /**
     * 时间窗口
     */
    protected long timeWindow;

    /**
     * 限定次数
     */
    protected int limitCount;

    /**
     * 构造一个限定时间窗口内只能访问指定次数的限定器，比如 1分钟内只允许调用 10次， 即 new RedisRateLimiter(60, 10)
     *
     * @param timeWindow 事件窗口，单位为 秒
     * @param limitCount 限定次数
     */
    public AbstractRateLimiter(long timeWindow, int limitCount) {
        this.timeWindow = timeWindow;
        this.limitCount = limitCount;
    }
}
