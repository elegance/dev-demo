package org.orh.basic;

/**
 * ThreadUtil
 *
 * @author ouronghui
 * @since 2022/8/24 19:05
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadUtil {

    private static final Log LOG = LogFactory.getLog(ThreadUtil.class);

    /**
     * 打印当前位置的调用栈信息
     * @param msg
     */
    public static void printStackTrace(String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        println(Thread.currentThread() + ", ★: [" + msg + "]");
        if (stackTrace.length < 2) {
            println("empty stack");
            return;
        }

        for (int i = 2; i < stackTrace.length; i++) {
            println("\tat " + stackTrace[i]);
        }
    }

    private static void println(Object object) {
        LOG.info(object);
    }

}