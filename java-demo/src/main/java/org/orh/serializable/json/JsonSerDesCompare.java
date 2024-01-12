package org.orh.serializable.json;

import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * JsonSerDesCompare
 *
 * @author ouronghui
 * @since 2024/1/12 16:48
 */
//@BenchmarkMode(Mode.AverageTime)
@BenchmarkMode(Mode.All)
@Warmup(iterations = 1, time = 1) // 预热次数为n ， x s
@Measurement(iterations = 2, time = 2)  // 表示测量n次，每次测量时间为x秒
@Threads(4) // 线程数
@Fork(1)  // 表示Benchmark的测试会运行1轮。
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JsonSerDesCompare {

    static UserUseDate userUseDate = new UserUseDate();
    static UserUseLong userUseLong = new UserUseLong();

    static final Gson gs = (new GsonBuilder()).create();
    Blackhole blackhole = new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous.");


    static String fastjsonUserUseDateStr = "{\"activation_time\":1705049872258,\"create_time\":1705049872258,\"disabled_time\":1705049872258,\"id\":\"fa468ba2-6a38-4844-9751-d29a4fd88a6e\",\"source\":\"aa\",\"update_time\":1705049872258,\"user_id\":\"bb\"}";
    static String gsonUserUseDateStr = "{\"id\":\"fa468ba2-6a38-4844-9751-d29a4fd88a6e\",\"create_time\":\"Jan 12, 2024 4:57:52 PM\",\"update_time\":\"Jan 12, 2024 4:57:52 PM\",\"source\":\"aa\",\"user_id\":\"bb\",\"disabled_time\":\"Jan 12, 2024 4:57:52 PM\",\"activation_time\":\"Jan 12, 2024 4:57:52 PM\"}";

    static String fastjsonUserUseLongStr = "{\"activation_time\":1705052492101,\"create_time\":1705052492101,\"disabled_time\":1705052492101,\"id\":\"691a0f2a-2dfd-47a4-a5c0-20ded634f430\",\"source\":\"aa\",\"update_time\":1705052492101,\"user_id\":\"bb\"}";
    static String gsonUserUseLongStr = "{\"id\":\"691a0f2a-2dfd-47a4-a5c0-20ded634f430\",\"create_time\":1705052492101,\"update_time\":1705052492101,\"source\":\"aa\",\"user_id\":\"bb\",\"disabled_time\":1705052492101,\"activation_time\":1705052492101}";

    static {
        userUseDate.setId(UUID.randomUUID().toString());
        userUseDate.setCreate_time(new Date());
        userUseDate.setUpdate_time(new Date());
        userUseDate.setSource("aa");
        userUseDate.setUser_id("bb");
        userUseDate.setDisabled_time(new Date());
        userUseDate.setActivation_time(new Date());

        userUseLong.setId(UUID.randomUUID().toString());
        userUseLong.setCreate_time(new Date().getTime());
        userUseLong.setUpdate_time(new Date().getTime());
        userUseLong.setSource("aa");
        userUseLong.setUser_id("bb");
        userUseLong.setDisabled_time(new Date().getTime());
        userUseLong.setActivation_time(new Date().getTime());
    }

    @Test
    public void gen() {
        System.out.println(JSON.toJSONString(userUseDate));
        System.out.println(gs.toJson(userUseDate));
        System.out.println(JSON.toJSONString(userUseLong));
        System.out.println(gs.toJson(userUseLong));
    }

    @Benchmark
    public void fastSerializeDate(Blackhole blackhole) {
        String str = JSON.toJSONString(userUseDate);
        blackhole.consume(str);
    }

    @Benchmark
    public void gsonSerializeDate(Blackhole blackhole) {
        String str = gs.toJson(userUseDate);
        blackhole.consume(str);
    }

    @Benchmark
    public void fastDeSerializeLong(Blackhole blackhole) {
        UserUseLong user = JSON.parseObject(fastjsonUserUseLongStr, UserUseLong.class);
        blackhole.consume(user);
    }

    @Benchmark
    public void gsonDeSerializeLong(Blackhole blackhole) {
        UserUseLong user = gs.fromJson(gsonUserUseLongStr, UserUseLong.class);
        blackhole.consume(user);
    }

    @Benchmark
    public void fastSerializeLong(Blackhole blackhole) {
        String str = JSON.toJSONString(userUseLong);
        blackhole.consume(str);
    }

    @Benchmark
    public void gsonSerializeLong(Blackhole blackhole) {
        String str = gs.toJson(userUseLong);
        blackhole.consume(str);
    }

    @Benchmark
    public void fastDeSerializeDate(Blackhole blackhole) {
        UserUseDate user = JSON.parseObject(fastjsonUserUseLongStr, UserUseDate.class);
        blackhole.consume(user);
    }

    @Benchmark
    public void gsonDeSerializeDate(Blackhole blackhole) {
        UserUseDate userUseDate = gs.fromJson(gsonUserUseDateStr, UserUseDate.class);
        blackhole.consume(userUseDate);
    }

    public static void main(String[] args) throws RunnerException {
        // 图形化结果：http://deepoove.com/jmh-visual-chart/
        // https://jmh.morethan.io/
        Options opt = new OptionsBuilder()
                .include(JsonSerDesCompare.class.getSimpleName())
                .result("json-ser-result-long-date.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Test
    public void testStopWatchTest() {
        StopWatch stopWatch = new StopWatch();
        int n = 10000 * 10;

        fs(stopWatch, n, userUseDate);
        fd(stopWatch, n, fastjsonUserUseDateStr, UserUseDate.class);

        gs(stopWatch, n, userUseDate);
        gd(stopWatch, n, gsonUserUseDateStr, UserUseDate.class);

        System.out.println("\n");

        StopWatch stopWatch2 = new StopWatch();
        fs(stopWatch2, n, userUseLong);
        fd(stopWatch2, n, fastjsonUserUseLongStr, UserUseLong.class);

        gs(stopWatch2, n, userUseLong);
        gd(stopWatch2, n, gsonUserUseLongStr, UserUseLong.class);

        System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        System.out.println(stopWatch2.prettyPrint(TimeUnit.MILLISECONDS));


    }

    private void gd(StopWatch stopWatch, int n, String dstr, Class clazz) {
        stopWatch.start("gsonDeSerialize " + clazz.getSimpleName());
        for (int i = 0; i < n; i++) {
            blackhole.consume(gs.fromJson(dstr, clazz));
        }
        stopWatch.stop();
        System.out.println("gsonDeSerialize " + clazz.getSimpleName() + " ns/ops: " +  stopWatch.getLastTaskTimeNanos() / n);
    }

    private void gs(StopWatch stopWatch, int n, Object sobj) {
        stopWatch.start("gsonSerialize " + sobj.getClass().getSimpleName());
        for (int i = 0; i < n; i++) {
            blackhole.consume(gs.toJson(sobj));
        }
        stopWatch.stop();
        System.out.println("gsonSerialize " + sobj.getClass().getSimpleName() + " ns/ops: " + stopWatch.getLastTaskTimeNanos() / n);
    }

    private void fd(StopWatch stopWatch, int n, String dstr, Class clazz) {
        stopWatch.start("fastDeSerialize " + clazz.getSimpleName());
        for (int i = 0; i < n; i++) {
            blackhole.consume(JSON.parseObject(dstr, clazz));
        }
        stopWatch.stop();
        System.out.println("fastDeSerialize " + clazz.getSimpleName() + " ns/ops: " + stopWatch.getLastTaskTimeNanos() / n);
    }

    private void fs(StopWatch stopWatch, int n, Object sobj) {
        stopWatch.start("fastSerialize " + sobj.getClass().getSimpleName());
        for (int i = 0; i < n; i++) {
            blackhole.consume(JSON.toJSONString(sobj));
        }
        stopWatch.stop();
        System.out.println("fastSerialize " + sobj.getClass().getSimpleName() + " ns/ops: " + stopWatch.getLastTaskTimeNanos() / n);
    }
}
