package org.orh.spring.jdbc.example;

import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.orh.spring.jdbc.config.JdbcTemplateConfiguration;
import org.orh.spring.jdbc.model.Source;
import org.orh.spring.jdbc.model.SourceField;
import org.orh.spring.jdbc.model.Table;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * SubscribeMetaQuery
 *
 * @author ouronghui
 * @since 2023/11/15 16:31
 */
public class SubscribeMetaQuery {
    private static void getPrimary() {
        boolean isProd = true;

        String canalTemplateName = isProd ? JdbcTemplateConfiguration.TEMPLATE_CANAL_PROD : JdbcTemplateConfiguration.TEMPLATE_CANAL_QA;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JdbcTemplateConfiguration.class);
        JdbcTemplate canalJdbcTemplate = context.getBean(canalTemplateName, JdbcTemplate.class);
        List<Pair<String, String>> sourceConfigs = canalJdbcTemplate.query("select id, source_config from canal_sync where is_deleted = 0 and source_config is not null", (resultSet, i) -> new Pair<>(resultSet.getString(1), resultSet.getString(2)));

        Set<String> databaseKeys = new HashSet<>();
        for (Pair<String, String> pair : sourceConfigs) {
            String sourceConfig = pair.getValue();
            List<Source> sources = JSON.parseArray(sourceConfig, Source.class);
            for (Source source : sources) {
                if (source.isSubscribed()) {
                    SourceField sourceField = source.getFields().get(0);
                    String column = sourceField.getColumn();
                    databaseKeys.add(pair.getKey() + "    " + source.getDatabase() + "." + source.getTable() + ": " + column);
                }
            }
        }
        List<String> collect = databaseKeys.stream().sorted().collect(Collectors.toList());
        String s = JSON.toJSONString(collect);
        System.out.println(s);
    }

    public static void main(String[] args) {
        boolean isProd = true;
        if (isProd) {
            getPrimary();
            return;
        }

        String canalTemplateName = isProd ? JdbcTemplateConfiguration.TEMPLATE_CANAL_PROD : JdbcTemplateConfiguration.TEMPLATE_CANAL_QA;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JdbcTemplateConfiguration.class);
        JdbcTemplate canalJdbcTemplate = context.getBean(canalTemplateName, JdbcTemplate.class);
        List<String> sourceConfigs = canalJdbcTemplate.query("select source_config from canal_sync where is_deleted = 0 and source_config is not null", (resultSet, i) -> resultSet.getString(1));

        Map<String, Set<String>> dbTables = new HashMap<>();

        Set<String> databaseTables = new HashSet<>();
        for (String sourceConfig : sourceConfigs) {
            List<Source> sources = JSON.parseArray(sourceConfig, Source.class);
            for (Source source : sources) {
                databaseTables.add(source.getDatabase() + "." + source.getTable());

                dbTables.computeIfAbsent(source.getDatabase(), k -> new HashSet<>()).add(source.getTable());
            }
        }
        // List<String> v2Tables = databaseTables.stream().sorted().collect(Collectors.toList());
        // System.out.println(JSON.toJSONString(v2Tables));


        List<Pair<String, Table>> pairList = canalJdbcTemplate.query("select t1.source_db_name, t2.content " +
                "from canal_sync t1, " +
                "     canal_sync_properties t2 " +
                "where t1.id = t2.sync_id " +
                "  and t2.is_deleted = 0 " +
                "  and t1.is_deleted = 0 " +
                "  and t2.prop_type = 'SUBSCRIBE' " +
                "  and t1.source_config is null", (resultSet, i) -> {
            String db = resultSet.getString(1);
            String content = resultSet.getString(2);
            Table subscribeTable = JSON.parseObject(content, Table.class);

            return new Pair<>(db, subscribeTable);
        });

        for (Pair<String, Table> dbAndTable : pairList) {
            databaseTables.add(dbAndTable.getKey() + "." + dbAndTable.getValue().getName());
            dbTables.computeIfAbsent(dbAndTable.getKey(), k -> new HashSet<>()).add(dbAndTable.getValue().getName());
        }

        System.out.println(JSON.toJSONString(databaseTables));
        // System.out.println(JSON.toJSONString(dbTables));


        StringJoiner columnQuerySql = new StringJoiner(" union all \n");
        for (Map.Entry<String, Set<String>> entry : dbTables.entrySet()) {
            String schema = entry.getKey();

            String tableFilter = entry.getValue().stream()
                    .sorted()
                    .map(name -> "(" + name + ")")
                    .collect(Collectors.joining("|", schema + "\\\\.(", ")"));
            String simpleFilter = entry.getValue().stream()
                    .sorted()
                    .map(name -> schema + "." + name)
                    .collect(Collectors.joining(","));


            String tables = entry.getValue().stream()
                    .sorted()
                    .map(name -> "'" + name + "'")
                    .collect(Collectors.joining(", ", "(", ")"));

            System.out.println(schema);
            System.out.println("canal.instance.filter.regex=" + tableFilter);
            // System.out.println("filter.simple=" + simpleFilter);
            // System.out.println("tables:" + tables);

            String querySql = "select TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH,COLUMN_COMMENT  " +
                    "from information_schema.COLUMNS  " +
                    "where TABLE_SCHEMA = '" + schema + "'  " +
                    "  and TABLE_NAME in " + tables +
                    "  and DATA_TYPE in ('mediumtext', 'text', 'tinytext', 'longtext', 'blob', 'mediumblob', 'longblob')";
            columnQuerySql.add(querySql);
        }

        System.out.println("大字段查询   ###################################################################");
        // System.out.println(columnQuerySql);
        JdbcTemplate bizJdbcTemplate = context.getBean(JdbcTemplateConfiguration.TEMPLATE_BIZ_QA, JdbcTemplate.class);

        List<FieldInfo> fieldInfos = bizJdbcTemplate.query(columnQuerySql.toString(), (resultSet, n) -> new FieldInfo(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
        System.out.println(JSON.toJSONString(fieldInfos));
        Map<String, List<FieldInfo>> schemaAndFieldListMap = fieldInfos.stream().collect(Collectors.groupingBy(f -> f.schema));
        for (Map.Entry<String, List<FieldInfo>> schemaEntry : schemaAndFieldListMap.entrySet()) {
            Map<String, List<FieldInfo>> tableAndFieldMap = schemaEntry.getValue().stream().collect(Collectors.groupingBy(f -> f.table));

            String schemaTableColumns = tableAndFieldMap.entrySet().stream().map(tableEntry -> {
                String columns = tableEntry
                        .getValue().stream()
                        .map(f -> f.column)
                        .collect(Collectors.joining("/"));
                return schemaEntry.getKey() + "." + tableEntry.getKey() + ":" + columns;
            }).collect(Collectors.joining(","));
            System.out.println("#table field black filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)\n" +
                    "canal.instance.filter.black.field=" + schemaTableColumns);
        }

//        Map<String, List<FieldInfo>> tableFieldMap = fieldInfos.stream().collect(Collectors.groupingBy(f -> (f.schema + "." + f.table)));
//        for (Map.Entry<String, List<FieldInfo>> tableAndFileListEntry : tableFieldMap.entrySet()) {
//            System.out.println(tableAndFileListEntry.getKey() + ":" + tableAndFileListEntry
//                    .getValue().stream()
//                    .map(f -> f.column)
//                    .collect(Collectors.joining("/")));
//        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class FieldInfo {
        public String schema;
        public String table;
        public String column;
    }
}
