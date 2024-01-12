package org.orh.spring.jdbc.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * JdbcTemplateConfiguration
 *
 * @author ouronghui
 * @since 2023/11/15 16:46
 */
@Configuration
public class JdbcTemplateConfiguration {

    @Bean
    DataSource canalDataSourceProd() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://TODO:3306/canal_adapter?characterEncoding=utf8");
        dataSource.setUsername("TODO");
        dataSource.setPassword("TODO");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }

    @Primary
    @Bean
    DataSource canalDataSourceQa() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://TODO:3306/canal_adapter?characterEncoding=utf8");
        dataSource.setUsername("TODO");
        dataSource.setPassword("TODO");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }

    @Bean
    DataSource bizDataSourceQa() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://TODO/show_center?useUnicode=true&characterEncoding=utf-8&transformedBitIsBoolean=true&autoReconnect=true&failOverReadOnly=false&useSSL=false&allowMultiQueries=true");
        dataSource.setUsername("TODO");
        dataSource.setPassword("TODO");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }


    @Bean
    DataSource xxlJobDataSourceQa() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://TODO/xxl_job?useUnicode=true&characterEncoding=utf-8&transformedBitIsBoolean=true&autoReconnect=true&failOverReadOnly=false&useSSL=false&allowMultiQueries=true");
        dataSource.setUsername("TODO");
        dataSource.setPassword("TODO");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(10);
        // 需要有权限
        // dataSource.setConnectionInitSql("set sql_log_bin=0");
        return dataSource;
    }

    public static final String TEMPLATE_CANAL_PROD = "TEMPLATE_CANAL_PROD";

    public static final String TEMPLATE_CANAL_QA = "TEMPLATE_CANAL_QA";

    public static final String TEMPLATE_BIZ_QA = "TEMPLATE_BIZ_QA";

    public static final String TEMPLATE_XXL_JOB_QA = "TEMPLATE_XXL_JOB_QA";

    @Bean(name = TEMPLATE_CANAL_PROD)
    JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(canalDataSourceProd());
    }

    @Bean(name = TEMPLATE_CANAL_QA)
    JdbcTemplate qaJdbcTemplate() {
        return new JdbcTemplate(canalDataSourceProd());
    }

    @Bean(name = TEMPLATE_BIZ_QA)
    JdbcTemplate qaBizJdbcTemplate() {
        return new JdbcTemplate(bizDataSourceQa());
    }

    @Bean(name = TEMPLATE_XXL_JOB_QA)
    JdbcTemplate xxlJobJdbcTemplate() {
        return new JdbcTemplate(xxlJobDataSourceQa());
    }
}