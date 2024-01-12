package org.orh.memory.filter;

/**
 * AviatorScriptDemo
 *
 * @author ouronghui
 * @since 2023/12/21 11:11
 */

import cn.hutool.core.date.StopWatch;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.function.system.BigIntFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExpressEngineFilterTest {
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
        data2.put("age", "20");

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "tao");
        data3.put("gender", "female");
        data3.put("age", 18);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("limit", 10);
        // data4.put("value", "5");  // will throw error, Could not compare <String, 5> with <JavaType, limit, 10, java.lang.Integer>
        data4.put("value", 5);

        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        datas.add(data4);

        AviatorEvaluator.getInstance().useLRUExpressionCache(1024);
        // AviatorEvaluatorInstance evaluatorInstance = AviatorEvaluator.newInstance(EvalMode.ASM);

        // 自定义函数
        AviatorEvaluator.addFunction(new AbstractFunction() {

            final BigIntFunction bigIntFunction = new BigIntFunction();

            @Override
            public String getName() {
                return "int";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                return bigIntFunction.call(env, arg1);
            }
        });

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
    }
    // https://my.oschina.net/u/4090830/blog/10315584
    // Java 表达式引擎选型调研分析
    // AviatorScript/ MVEL (MVFLEX Expression Language)

    @Test
    public void testAviator2() {
        Map<String, Object> data = new HashMap<>();
        data.put("name1", null);
        data.put("name2", "");
        data.put("name3", " ");
        data.put("name4", "ok");
        data.put("name5", 1);
        data.put("name6", "string use == isEquals?");

        Assert.isTrue((Boolean) AviatorEvaluator.execute("isBlank(name1)", data), "fail");
        Assert.isTrue((Boolean) AviatorEvaluator.execute("isBlank(name2)", data), "fail");
        Assert.isTrue((Boolean) AviatorEvaluator.execute("isBlank(name3)", data), "fail");
        assertThat((Boolean) AviatorEvaluator.execute("isBlank(name4)", data), equalTo(false));
        assertThat((Boolean) AviatorEvaluator.execute("name6 == 'string use == isEquals?'", data), equalTo(true));

        assertThat((Boolean) AviatorEvaluator.execute("include(tuple(1, 2), 1)", data), equalTo(true));
        assertThat((Boolean) AviatorEvaluator.execute("include(tuple(1, 2), 2)", data), equalTo(true));
        assertThat((Boolean) AviatorEvaluator.execute("include(tuple(1, 3), 2)", data), equalTo(false));
        assertThat((Boolean) AviatorEvaluator.execute("include(tuple(1, 2), name5)", data), equalTo(true));
        assertThat((Boolean) AviatorEvaluator.execute("!include(tuple(1, 2), name5)", data), equalTo(false));

    }

    // google aviator
    @Test
    public void testAviator() {
//        我有一些 sql 条件，需要转换为 aviatorscript 来表示，在后续的对话中，我会发给你sql条件，请返回给我转换后的结果，有以下转换规则
//        1. = 替换为 ==
//        2. or 替换为 || ; and 替换为 &&
//        3. xx is null or xx = '' 替换为 isBlank(xx)
//        4. xx in (1, 2) 这种形式的替换为 include(tuple(1, 2), xx)；如果是 not in ，在include 前加上 ! 号即可
//        5. 由于传入的 xx 都是字符串类型，如果原与xx比较符为数字，将数字用单引号包裹起来(转为字符串)，如 a = 1 转成 a == '1', 如 xx in (1, 2) 转成 include(tuple('1', '2'), xx)

        // 查询 schema 信息，得到表字段类型、主键、索引， 用以后续校验
        // show create table 脚本来解析 字段？
        // jdbc getObject


//        // https://www.yuque.com/boyan-avfmj/aviatorscript/ashevw
        // 函数，long 将字符串类型的 age 转为 long
        Blackhole blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");


        int n = 10000;
        String expressionStr = "((gender == 'male' && int(age) > 20) || (gender == 'female' && int(age) > 18) || (value < limit))";


        StopWatch stopWatch = new StopWatch();
        stopWatch.start("compile_cache");
        Expression expression = AviatorEvaluator.compile(expressionStr, true);

        for (int i = 0; i < n; i++) {
            List<Map<String, Object>> filteredData1 = datas.stream().filter(it -> (boolean) expression.execute(it)).collect(Collectors.toList());
            blackhole.consume(filteredData1);
        }
        stopWatch.stop();
        stopWatch.start("direct");

        for (int i = 0; i < n; i++) {
            List<Map<String, Object>> filteredData2 = datas.stream().filter(it -> (boolean) AviatorEvaluator.execute(expressionStr, it)).collect(Collectors.toList());
            blackhole.consume(filteredData2);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testSPEL() {
        // 非 javaBean 不支持直接访问
        // String expressionString = "((gender == 'male' && age > 20) || (gender == 'female' && age > 18))";
        String expressionString = "((#this['gender'] == 'male' && #this['age'] > 20) || (#this['gender'] == 'female' && #this['age'] > 18))";
        // 如需要空值判断
        //  String expressionString = "(#this.containsKey('gender') && (#this['gender'] == 'male' && #this['age'] > 20)) || (#this.containsKey('gender') && (#this['gender'] == 'female' && #this['age'] > 18))";

        ExpressionParser parser = new SpelExpressionParser();
        org.springframework.expression.Expression expression = parser.parseExpression(expressionString);
        List<Map<String, Object>> filteredData = datas.stream().filter(it -> expression.getValue(it, Boolean.class)).collect(Collectors.toList());
        System.out.println(filteredData);
    }

    @SneakyThrows
    @Test
    public void qlExpress() {
        List<Map<String, Object>> filteredData = new ArrayList<>();
        String expressionString = "((gender == 'male' && age > 20) || (gender == 'female' && age > 18))";

        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();

        for (Map<String, Object> data : datas) {
            context.clear();
            context.putAll(data);

            Object result = runner.execute(expressionString, context, null, true, false);
            if (result instanceof Boolean && (Boolean) result) {
                filteredData.add(data);
            }
        }

        System.out.println(filteredData);
    }

    public void test() {

    }

    // MessageEvaluationContext rocketmq tag filter sql92?
    // h2 memory db

    // cq engine
    // https://github.com/npgall/cqengine
    // Calcite : https://www.jianshu.com/p/d8977aab8821
}
