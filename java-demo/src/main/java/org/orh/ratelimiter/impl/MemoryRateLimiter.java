package org.orh.ratelimiter.impl;

import org.orh.ratelimiter.RateLimiter;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author orh
 * @description 内存实现版，TODO 待测试
 * @date 2018/4/4 9:58
 */
public class MemoryRateLimiter implements RateLimiter {

    private Map<String, Queue<Long>> storedMap = new ConcurrentHashMap<>();

    /**
     * 时间窗口
     */
    private long timeWindow;

    /**
     * 限定次数
     */
    private int limitCount;

    /**
     * 构造一个限定时间窗口内只能访问指定次数的限定器，比如 1分钟内只允许调用 10次， 即 new RedisRateLimiter(60, 10)
     *
     * @param timeWindow 事件窗口，单位为 秒
     * @param limitCount 限定次数
     */
    public MemoryRateLimiter(long timeWindow, int limitCount) {
        this.timeWindow = timeWindow;
        this.limitCount = limitCount;
    }

    @Override
    public boolean canInvoke(String invokeSignature) {
        long currentTime = System.currentTimeMillis();

        // 首次访问
        if (!storedMap.containsKey(invokeSignature)) {
            // 队列的最大长度限定为 限定的次数
            Queue<Long> queue = new ArrayBlockingQueue<>(limitCount);
            queue.add(currentTime);
            storedMap.put(invokeSignature, queue);
            return true;
        }

        Queue<Long> queue = storedMap.get(invokeSignature);
        Long firstTime = queue.peek();

        // 第一次访问与现在的时间已经超过时间窗口
        if ((currentTime - firstTime) > timeWindow) {
            // 队列满了将老数据清除出去
            if (queue.size() == limitCount) {
                queue.remove();
            }
            queue.add(currentTime);
            return true;
        }

        // 此处最大 只能是 等于
        if (queue.size() >= limitCount) {
            queue.remove();
            queue.add(currentTime);
            return false;
        }
        return true;
    }

    public String getSignature(String method, String userId) {
        return method + ":" + userId;
    }
}
