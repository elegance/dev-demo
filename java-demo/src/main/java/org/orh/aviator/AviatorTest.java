package org.orh.aviator;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorNumber;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.utils.Env;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * AviatorTest
 *
 * @author ouronghui
 * @since 2024/1/13 11:36
 */
public class AviatorTest {
    static {
        AviatorEvaluator.getInstance().useLRUExpressionCache(1024);
        // AviatorEvaluatorInstance evaluatorInstance = AviatorEvaluator.newInstance(EvalMode.ASM);

        // 自定义函数

        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "isBlank";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                String value = FunctionUtils.getStringValue(arg1, env);
                return StringUtils.isBlank(value) ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
            }
        });

        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "toNumber";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                return call(env, arg1, null);
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject defaultValArg) {
                Number defaultVal = null;
                if (defaultValArg !=null && !defaultValArg.isNull(env)) {
                    defaultVal = defaultValArg.numberValue(env);
                }

                Object obj = arg1.getValue(env);
                switch (arg1.getAviatorType()) {
                    case Boolean:
                        return AviatorRuntimeJavaType.valueOf(arg1.booleanValue(env) ? 1 : 0);
                    case JavaType:
                        if (obj instanceof Number) {
                            return AviatorRuntimeJavaType.valueOf(obj);
                        }
                    default:
                        return AviatorRuntimeJavaType.valueOf(NumberUtil.parseNumber(String.valueOf(obj), defaultVal));
                }
            }
        });

        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "toMap";
            }


            @Override
            public AviatorObject call(Map<String, Object> env) {
                return call(env, null);
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject isIncludeModeArg) {
                return call(env, isIncludeModeArg, null);
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject isIncludeModeArg, AviatorObject columnsStrArg) {
                if (isIncludeModeArg == null) {
                    isIncludeModeArg = AviatorBoolean.TRUE;
                }
                boolean isInclude = FunctionUtils.getBooleanValue(isIncludeModeArg, env);
                Map<String, Object> data = ((Env) env).getDefaults();
                String columnsStr = null;
                if (columnsStrArg != null) {
                    columnsStr = FunctionUtils.getStringValue(columnsStrArg, env);
                }
                if (StringUtils.isNotBlank(columnsStr)) {
                    Set<String> columns = Arrays.stream(StringUtils.split(columnsStr, ',')).map(String::trim).collect(Collectors.toSet());
                    if (isInclude) {
                        data = data.entrySet().stream().filter(entry -> columns.contains(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    } else {
                        data = data.entrySet().stream().filter(entry -> !columns.contains(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    }
                }
                return AviatorRuntimeJavaType.valueOf(data);
            }
        });

        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public String getName() {
                return "geo_point";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject lonArg, AviatorObject latArg) {
                Number lon = FunctionUtils.getNumberValue(lonArg, env);
                Number lat = FunctionUtils.getNumberValue(latArg, env);
                HashMap<String, Number> coordinate = new HashMap<String, Number>() {{
                    put("lon", lon);
                    put("lat", lat);
                }};
                return AviatorRuntimeJavaType.valueOf(coordinate);
            }
        });
    }

    @Test
    public void testToMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("name1", "string use == isEquals?");
        data.put("name2", 121.493086);
        data.put("name3", 31.195752);

        Object objG = AviatorEvaluator.execute("toMap()", data);
        System.out.println("objG: "+ JSON.toJSONString(objG));

        Object obj = AviatorEvaluator.execute("toMap(true)", data);
        assertThat(obj.getClass().getName(), equalTo("java.util.HashMap"));

        // include 模式, 仅包含 name1, name2
        Map<String, Object> obj2 = (Map<String, Object>) AviatorEvaluator.execute("toMap(true, 'name1,name2')", data);
        assertThat(Sets.difference(obj2.keySet(), Sets.newHashSet("name1", "name2")).size(), equalTo(0));

        // include 模式, 但未include 任何值，此时会返回 所有！！！
        Map<String, Object> obj1 = (Map<String, Object>) AviatorEvaluator.execute("toMap(true)", data);
        assertThat(Sets.difference(obj1.keySet(), Sets.newHashSet("name1", "name2", "name3")).size(), equalTo(0));

        // exclude 模式, 一个都位排除
        Map<String, Object> obj3 = (Map<String, Object>) AviatorEvaluator.execute("toMap(false)", data);
        assertThat(Sets.difference(obj3.keySet(), Sets.newHashSet("name1", "name2", "name3")).size(), equalTo(0));

        // exclude 模式, 仅排除 name1
        Map<String, Object> obj4 = (Map<String, Object>) AviatorEvaluator.execute("toMap(false, 'name1')", data);
        assertThat(Sets.difference(obj4.keySet(), Sets.newHashSet("name2", "name3")).size(), equalTo(0));


        System.out.println(obj);
        System.out.println(obj2);
        System.out.println(obj3);
        System.out.println(obj4);
        System.out.println(obj.getClass().getName());

    }

    @Test
    public void testCoordinate() {
        Map<String, Object> cordMap = new HashMap<>();
        cordMap.put("name2", 121.493086);
        cordMap.put("name3", 31.195752);
        cordMap.put("name4", "31.195752");
        cordMap.put("name5", new BigDecimal("112.23"));
        cordMap.put("name6", "x");

        Map<String, Object> coordinate = (Map<String, Object>) AviatorEvaluator.execute("geo_point(name2, name3)", cordMap);
        System.out.println(coordinate);
        coordinate = (Map<String, Object>) AviatorEvaluator.execute("geo_point(name2, toNumber(name4))", cordMap);
        System.out.println(coordinate);
        coordinate = (Map<String, Object>) AviatorEvaluator.execute("geo_point(11.2, toNumber(name4))", cordMap);
        System.out.println(coordinate);
        coordinate = (Map<String, Object>) AviatorEvaluator.execute("geo_point(toNumber('11.2'), toNumber(name4))", cordMap);
        System.out.println(coordinate);
        coordinate = (Map<String, Object>) AviatorEvaluator.execute("geo_point(toNumber('11.2'), toNumber(true))", cordMap);
        System.out.println(coordinate);
        coordinate = (Map<String, Object>) AviatorEvaluator.execute("geo_point(toNumber('11.2'), toNumber(name6, 223))", cordMap);
        System.out.println(coordinate);
    }

    @Test
    public void testAviator2() {
        Map<String, Object> data = new HashMap<>();
        data.put("name1", null);
        data.put("name2", "");
        data.put("name3", " ");
        data.put("name4", "ok");
        data.put("name5", 1);
        data.put("name6", "string use == isEquals?");
        data.put("name7", 121.493086);
        data.put("name8", 31.195752);

        Assert.isTrue((Boolean) AviatorEvaluator.execute("isBlank(name1)", data), "fail");
        Assert.isTrue((Boolean) AviatorEvaluator.execute("isBlank(name2)", data), "fail");
        Assert.isTrue((Boolean) AviatorEvaluator.execute("isBlank(name3)", data), "fail");
        assertThat((Boolean) AviatorEvaluator.execute("isBlank(name4)", data), equalTo(false));
        assertThat((Boolean) AviatorEvaluator.execute("name6 == 'string use == isEquals?'", data), equalTo(true));

        assertThat((Boolean) AviatorEvaluator.execute("include(tuple(1, 2), 1)", data), equalTo(true));
        assertThat((Boolean) AviatorEvaluator.execute("include(tuple(1, 2), 2)", data), equalTo(true));
        assertThat((Boolean) AviatorEvaluator.execute("include(tuple(1, 3), 2)", data), equalTo(false));
        assertThat((Boolean) AviatorEvaluator.execute("include(tuple(1, 2), name5)", data), equalTo(true));
        assertThat((Boolean) AviatorEvaluator.execute("!include(tuple(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43), name5)", data), equalTo(false));

        // for geo_point
        assertThat((String) AviatorEvaluator.execute("name7 + ',' + name8", data), equalTo("121.493086,31.195752"));

        // for object
        String expression = "let m = seq.map(); \n" +
                "for entry in __env__ { \n" +
                "   ## m 也会在 env 中，下面对 为m的key做了排除 \n" +
                "   if entry.key == 'm' { \n" +
                "   continue; \n" +
                "   } \n" +
                "   seq.put(m, '' + entry.key, entry.value); \n" +
                "} \n" +
                "return m; \n";
        Object obj = AviatorEvaluator.execute(expression, data, true);
        System.out.println("obj type: " + obj.getClass().getName());

        assertThat(obj.getClass().getName(), equalTo("java.util.HashMap"));

        Map<String, Object> map = (Map<String, Object>) obj;
        System.out.println(map);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("eval");
        int n = 1 * 10000;
        for (int i = 0; i < n; i++) {
            map = (Map<String, Object>) AviatorEvaluator.execute(expression, data, true);
        }
        stopWatch.stop();
        System.out.println("ops/millis :" + (stopWatch.getLastTaskTimeMillis() / n));
        System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }
}
