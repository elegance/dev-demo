package org.orh.jmh;

import com.alibaba.otter.canal.filter.aviater.AviaterRegexFilter;
import com.alibaba.otter.canal.filter.aviater.AviaterSimpleFilter;
import lombok.SneakyThrows;
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

import java.util.concurrent.TimeUnit;

/**
 * FilterModeTest
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
public class FilterModeTest {

    @Param(value = {"tm_show.fulfillment_order", "tm_show.fulfillment_order1", "tm_show.tm_purchase_order", "tm_show.tm_purchase_order1", "tm_show.tm_transfer_order", "tm_show.tm_transfer_order1"})
    private String table;

    AviaterRegexFilter regexFilter = new AviaterRegexFilter("tm_show\\.((fulfillment_order)|(tm_artist_show)|(tm_city)|(tm_demand_market)|(tm_location)|(tm_order)|(tm_order_item)|(tm_order_item_ticket)|(tm_po_ticket_stub)|(tm_pre_delivery_orders)|(tm_purchase_order)|(tm_purchase_order_freeze)|(tm_purchase_order_history)|(tm_seat_plan)|(tm_seller)|(tm_seller_settlementorder)|(tm_seller_transaction)|(tm_session_overdue_ctrl)|(tm_show)|(tm_show_session)|(tm_site_city)|(tm_transfer_order))");

    AviaterSimpleFilter simpleFilter = new AviaterSimpleFilter("tm_show.fulfillment_order,tm_show.tm_artist_show,tm_show.tm_city,tm_show.tm_demand_market,tm_show.tm_location,tm_show.tm_order,tm_show.tm_order_item,tm_show.tm_order_item_ticket,tm_show.tm_po_ticket_stub,tm_show.tm_pre_delivery_orders,tm_show.tm_purchase_order,tm_show.tm_purchase_order_freeze,tm_show.tm_purchase_order_history,tm_show.tm_seat_plan,tm_show.tm_seller,tm_show.tm_seller_settlementorder,tm_show.tm_seller_transaction,tm_show.tm_session_overdue_ctrl,tm_show.tm_show,tm_show.tm_show_session,tm_show.tm_site_city,tm_show.tm_transfer_order");

    @SneakyThrows
    @Benchmark
    public void testRegFilter(Blackhole blackhole) {
        boolean filter = regexFilter.filter(table);

        // JVM可能会认为变量a从来没有使用过，从而进行优化把整个方法内部代码移除掉，显然，这影响了测试结果。
        // JMH提供了两种方式避免这种问题，一种是将这个变量作为方法返回值return a，一种是通过Blackhole类来消费这个变量：
        blackhole.consume(filter);
    }

    @SneakyThrows
    @Benchmark
    public void testSimFilter(Blackhole blackhole) {
        boolean filter = simpleFilter.filter(table);

        // JVM可能会认为变量a从来没有使用过，从而进行优化把整个方法内部代码移除掉，显然，这影响了测试结果。
        // JMH提供了两种方式避免这种问题，一种是将这个变量作为方法返回值return a，一种是通过Blackhole类来消费这个变量：
        blackhole.consume(filter);
    }

    public static void main(String[] args) throws RunnerException {
        // 图形化结果：http://deepoove.com/jmh-visual-chart/
        // https://jmh.morethan.io/
        Options opt = new OptionsBuilder()
                .include(FilterModeTest.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }
}
