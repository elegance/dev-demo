package org.orh.basic;

/**
 * MapToJsonString
 *
 * @author ouronghui
 * @since 2024/1/20 17:33
 */

import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson.JSON;
import org.apache.groovy.util.Maps;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MapToJsonString {

    public static String toJson(Object value) {
        if (value == null) {
            return "{}";
        } else if (value instanceof String) {
            //return "\"" + escapeJsonString((String)value) + "\"";
            return "\"" + value + "\"";
        } else if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof Map) {
            return mapToJson((Map<String, Object>) value);
        } else if (value instanceof Iterable) {
            return listToJson((Iterable<?>) value);
        } else {
            // 对于其他复杂类型（例如自定义对象），这里仅给出简单处理，通常需要更复杂的逻辑
            return "\"Unsupported Type\"";
        }
    }

    private static String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");

        Set<Map.Entry<String, Object>> entries = map.entrySet();
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : entries) {
            if (!isFirst) {
                sb.append(",");
            } else {
                isFirst = false;
            }

            sb.append("\"").append(entry.getKey()).append("\":").append(toJson(entry.getValue()));
        }

        sb.append("}");
        return sb.toString();
    }

    private static String listToJson(Iterable<?> list) {
        StringBuilder sb = new StringBuilder("[");
        boolean isFirst = true;
        for (Object item : list) {
            if (!isFirst) {
                sb.append(",");
            } else {
                isFirst = false;
            }
            sb.append(toJson(item));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String escapeJsonString(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("/", "\\/")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zs");
        map.put("age", 20);
        map.put("address", Maps.of("city", "Beijing", "country", "China"));
        map.put("hobbies", Arrays.asList("reading", "swimming"));

        System.out.println(mapToJson(map));
        System.out.println(JSON.toJSONString(map));


        Blackhole blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");
        StopWatch stopWatch = new StopWatch();

        final int n = 1000000;


        stopWatch.start("fastjson");
        for (int i = 0; i < n; i++) {
            blackhole.consume(JSON.toJSONString(map));
        }
        stopWatch.stop();

        stopWatch.start("strAppend");
        for (int i = 0; i < n; i++) {
            blackhole.consume(mapToJson(map));
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }
}

