package org.orh.ratelimiter;

/**
 * @author orh
 * @description
 * @date 2018/4/4 9:54
 */
public interface RateLimiter {
    boolean canInvoke(String invokeSignature);
}
