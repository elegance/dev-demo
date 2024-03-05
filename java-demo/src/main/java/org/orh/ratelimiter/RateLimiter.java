package org.orh.ratelimiter;

/**
 * @author orh
 * @description       https://mp.weixin.qq.com/s/W1ab03LDA1--54GFf5_lig
 * @date 2018/4/4 9:54
 */
public interface RateLimiter {
    boolean canInvoke(String invokeSignature);
}
