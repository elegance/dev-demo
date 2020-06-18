package org.orh.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 字符串拼接测试
 *
 * @author ouronghui
 * @since 2020/6/18
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1) // 预热次数为5 ， 1s
@Measurement(iterations = 5, time = 5)  // 表示测量5次，每次测量时间为5秒
@Threads(4) // 线程数
@Fork(1)  // 表示Benchmark的测试会运行1轮。
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StringConnectTest {

    /**
     * 分别遍历 10， 50， 100 次
     */
    @Param(value = {"10", "50", "100"})
    private int length;

    @Benchmark
    public void testStringAdd(Blackhole blackhole) {
        String a = "";
        for (int i = 0; i < length; i++) {
            a += i;
        }
        // JVM可能会认为变量a从来没有使用过，从而进行优化把整个方法内部代码移除掉，显然，这影响了测试结果。
        // JMH提供了两种方式避免这种问题，一种是将这个变量作为方法返回值return a，一种是通过Blackhole类来消费这个变量：
        blackhole.consume(a);
    }

    @Benchmark
    public void testStringBuilderAdd(Blackhole blackhole) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(i);
        }
        blackhole.consume(sb.toString());
    }

    public static void main(String[] args) throws RunnerException {
        // 图形化结果：http://deepoove.com/jmh-visual-chart/
        // https://jmh.morethan.io/
        Options opt = new OptionsBuilder()
                .include(StringConnectTest.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }
}
