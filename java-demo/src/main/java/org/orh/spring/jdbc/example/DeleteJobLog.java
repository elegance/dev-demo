package org.orh.spring.jdbc.example;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.orh.spring.jdbc.config.JdbcTemplateConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;

/**
 * DeleteJobLog
 *
 * @author ouronghui
 * @since 2023/12/8 11:49
 */
public class DeleteJobLog {
    // 滚动删除
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JdbcTemplateConfiguration.class);
        JdbcTemplate jobJdbcTemplate = context.getBean(JdbcTemplateConfiguration.TEMPLATE_XXL_JOB_QA, JdbcTemplate.class);
        DateTime endDate = DateUtil.parse("2023-12-10 00:00:00");

        while (true) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("queryMinTime");
            String minDateStr = jobJdbcTemplate.queryForObject("select min(trigger_time) from xxl_job.xxl_job_log ", String.class);
            stopWatch.stop();

            DateTime cur = DateUtil.parseDateTime(minDateStr);
            if (cur.isAfter(endDate)) {
                break;
            }
            String curStr = DateUtil.formatDate(cur);
            String start = curStr + " 00:00:00";
            String end = curStr + " 23:59:59";

            String delSql = "delete from xxl_job.xxl_job_log where trigger_time >= '" + start + "' and trigger_time <= '" + end + "'";
            stopWatch.start("deleteData");
            jobJdbcTemplate.execute(delSql);
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        }
    }
}
