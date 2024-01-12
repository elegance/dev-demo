package org.orh.basic;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

/**
 * LogConfUtil
 *
 * @author ouronghui
 * @since 2023/12/28 16:03
 */
public class LogConfUtil {
    public static void configLogBack() {
        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            String logConfig =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<configuration  debug=\"true\">\n" +
                            "    <contextName>logback</contextName>\n" +
                            "    <!--日志输出到控制台规则 -->\n" +
                            "    <appender name=\"console\" class=\"ch.qos.logback.core.ConsoleAppender\">\n" +
                            "        <encoder>\n" +
                            "            <pattern> %d[%level]%c{100}.%M:%L%m%n </pattern>\n" +
                            "        </encoder>\n" +
                            "    </appender>\n" +
                            "    <root level=\"INFO\">\n" +
                            "        <appender-ref ref=\"console\"/>\n" +
                            "    </root>\n" +
                            "</configuration>";
            configurator.doConfigure(new ByteArrayInputStream(logConfig.getBytes()));
            StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
        } catch (JoranException e) {
            throw new RuntimeException(e);
        }
    }
}
