package org.orh.memory.filter;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import lombok.SneakyThrows;
import org.junit.Test;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * HsqlDBTest
 *
 * @author ouronghui
 * @since 2023/12/20 16:00
 */
public class HsqlDBTest {

    @SneakyThrows
    @Test
    public void test() {
        SimpleDataSource dataSource = new SimpleDataSource("jdbc:hsqldb:mem:memdbid", "SA", "", "org.hsqldb.jdbcDriver");
        Blackhole blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");

        StopWatch stopwatch = new StopWatch();
        stopwatch.start("create");
        int len = 10000;

        for (int i = 0; i < len; i++) {
            String tableName = "t_" + UUID.fastUUID().toString().replace("-", "");

            DbUtil.use(dataSource).execute("CREATE TABLE " + tableName + "(id int,name varchar(20),age int,birthday bigint)");
            DbUtil.use(dataSource).execute("INSERT INTO " + tableName + "(id,name,age,birthday) VALUES(1,'fucheng',58,1646387235000)");
            DbUtil.use(dataSource).execute("INSERT INTO " + tableName + "(id,name,age,birthday) VALUES(2,'fucheng',58,1646387235000)");
            List<Entity> query = DbUtil.use(dataSource).query("SELECT sum(t1.age) tt FROM " + tableName +" t1");
            Set<Map.Entry<String, Object>> entries = query.get(0).entrySet();
            blackhole.consume(query);
        }
        stopwatch.stop();
        stopwatch.start("create");
        System.out.println(stopwatch.prettyPrint(TimeUnit.MILLISECONDS));

        for (int i = 0; i < len; i++) {
            String tableName = "t_" + UUID.fastUUID().toString().replace("-", "");

            DbUtil.use(dataSource).execute("CREATE TABLE " + tableName + "(id int,name varchar(20),age int,birthday bigint)");
            DbUtil.use(dataSource).execute("INSERT INTO " + tableName + "(id,name,age,birthday) VALUES(1,'fucheng',58,1646387235000)");
            DbUtil.use(dataSource).execute("INSERT INTO " + tableName + "(id,name,age,birthday) VALUES(2,'fucheng',58,1646387235000)");
            List<Entity> query = DbUtil.use(dataSource).query("SELECT sum(t1.age) tt FROM " + tableName +" t1");
            DbUtil.use(dataSource).execute("drop table " + tableName);
            blackhole.consume(query);
        }
        stopwatch.stop();
        System.out.println(stopwatch.prettyPrint(TimeUnit.MILLISECONDS));

        dataSource.close();

    }

    @SneakyThrows
    @Test
    public void customFun() {
        SimpleDataSource dataSource = new SimpleDataSource("jdbc:hsqldb:mem:memdbid", "SA", "", "org.hsqldb.jdbcDriver");
        DbUtil.use(dataSource).execute("CREATE FUNCTION calculate(a INT,b INT) RETURNS CHAR VARYING(100) LANGUAGE JAVA DETERMINISTIC NO SQL EXTERNAL NAME 'CLASSPATH:org.orh.hsqldb.HsqlDBTest.calculate'");
        DbUtil.use(dataSource).execute("CREATE TABLE tmp1(id int,name varchar(20),age int,birthday bigint)");
        DbUtil.use(dataSource).execute("CREATE TABLE tmp2(id int,name varchar(20),age int,birthday bigint)");
        DbUtil.use(dataSource).execute("INSERT INTO tmp1(id,name,age,birthday) VALUES(1,'fucheng',58,1646387235000)");
        DbUtil.use(dataSource).execute("INSERT INTO tmp2(id,name,age,birthday) VALUES(1,'fucheng',23,1646387235000)");
        DbUtil.use(dataSource).execute("INSERT INTO tmp2(id,name,age,birthday) VALUES(2,'zhangsan',24,1646387235000)");
        List<Entity> query = DbUtil.use(dataSource).query("SELECT calculate(t1.age,t2.age) tt FROM tmp1 t1 RIGHT JOIN tmp2 t2 ON t1.id=t2.id");
        Console.log(query);
        dataSource.close();
    }

    public static String calculate(Integer a, Integer b) {
        if (a == null) {
            return "0%";
        } else if (b == null) {
            return "100%";
        }
        double calculate = NumberUtil.calculate(StrUtil.format("({}-{})/{}", a, b, b));
        return NumberUtil.decimalFormat("#.##%", calculate);
    }
}
