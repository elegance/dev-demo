package org.orh.memory.filter;

import cn.hutool.core.date.StopWatch;
import com.mysql.cj.util.LogUtils;
import lombok.SneakyThrows;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.config.NullCollation;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.openjdk.jmh.infra.Blackhole;
import org.orh.basic.LogConfUtil;
import org.orh.memory.filter.calcite.MemoryColumn;
import org.orh.memory.filter.calcite.MemorySchema;
import org.orh.memory.filter.calcite.MemoryTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * CalciteFilterTest
 *
 * @author ouronghui
 * @since 2023/12/27 19:51
 */
public class CalciteFilterTest {
    static List<Map<String, Object>> datas = new ArrayList<>();

    static {
        // 定义数据
        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "li");
        data1.put("gender", "male");
        data1.put("age", 22);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "merry");
        data2.put("gender", "female");
        data2.put("age", 20);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "tao");
        data3.put("gender", "female");
        data3.put("age", 18);


        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
    }

    @SneakyThrows
    public static void main(String[] args) {
        LogConfUtil.configLogBack();

        Blackhole blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");


        List<MemoryColumn> meta = new ArrayList<>();
        MemoryColumn<String> id = new MemoryColumn<>("name", String.class);
        MemoryColumn<String> name = new MemoryColumn<>("gender", String.class);
        MemoryColumn<Integer> age = new MemoryColumn<>("age", Integer.class);

        meta.addAll(Arrays.asList(id, name, age));

        List<List<Object>> rows = datas.stream().map(data -> meta.stream().map(m -> data.get(m.getName())).collect(Collectors.toList())).collect(Collectors.toList());

        List<List<Object>> matchRows = getData(meta, rows, "(gender = 'female' and age > 18) or (gender = 'male' and age > 20)");
        System.out.println(matchRows);
        for (List<Object> matchRow : matchRows) {
            for (Object field : matchRow) {
                System.out.println(field.getClass() + ", value: " + field);
            }
        }
        int n = 10000;
        StopWatch watch = new StopWatch();
        watch.start("query");
        for (int i = 0; i < n; i++) {
            List<List<Object>> results = getData(meta, rows, "(gender = 'female' and age > 18) or (gender = 'male' and age > 20)");
            blackhole.consume(results);
        }
        watch.stop();
        System.out.println(watch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    static MemorySchema memorySchema;

    public static List<List<Object>> getData(List<MemoryColumn> meta, List<List<Object>> source, String condition) throws SQLException {
        if (memorySchema == null) {
            memorySchema = new MemorySchema(meta, source);
        } else {
            // 构造Schema
            MemoryTable table = (MemoryTable) memorySchema.getTableMap().get("memory");
            table.setSource(source);
        }

        // 设置连接参数
        Properties info = new Properties();
        info.setProperty(CalciteConnectionProperty.DEFAULT_NULL_COLLATION.camelName(), NullCollation.LAST.name());
        info.setProperty(CalciteConnectionProperty.CASE_SENSITIVE.camelName(), "false");
        // 建立连接
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        // 执行查询
         Statement statement = connection.createStatement();
        String sql = "select * from memory.memory where " + condition;

        // PreparedStatement statement = connection.prepareStatement(sql);
        // 取得Calcite连接
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        // 取得RootSchema RootSchema是所有Schema的父Schema
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        // 添加schema
        rootSchema.add("memory", memorySchema);
        // 编写SQL
        // ResultSet resultSet = statement.executeQuery(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        List<List<Object>> matchRows = new ArrayList<>();
        while (resultSet.next()) {
            matchRows.add(Arrays.asList(resultSet.getObject(1), resultSet.getObject(2), resultSet.getObject(3)));
            // System.out.println(resultSet.getObject(1) + ":" + resultSet.getObject((2) + ":" + resultSet.getObject(3)));
        }

        resultSet.close();
        statement.close();
        connection.close();
        return matchRows;
    }

}
