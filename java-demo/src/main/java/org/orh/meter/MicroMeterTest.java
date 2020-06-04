package org.orh.meter;

import com.alibaba.fastjson.JSON;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertEquals;

/**
 * 参考：使用 Micrometer 记录 Java 应用性能指标 - https://www.ibm.com/developerworks/cn/java/j-using-micrometer-to-record-java-metric/index.html
 *
 * @author ouronghui
 * @since 2020/6/4
 */
public class MicroMeterTest {
    /**
     * CompositeMeterRegistry 使用示例
     */
    @Test
    public void test_01() {
        CompositeMeterRegistry registry = new CompositeMeterRegistry();
        registry.add(new SimpleMeterRegistry());
        registry.add(new SimpleMeterRegistry(new MyConfig(), Clock.SYSTEM));

        Counter counter = registry.counter("simple");
        counter.increment();
        System.out.println(JSON.toJSONString(registry.getMeters()));
    }

    /**
     * 使用全局计量器注册表对象
     */
    @Test
    public void test_02() {
        Metrics.addRegistry(new SimpleMeterRegistry());
        Counter counter = Metrics.counter("simple");
        counter.increment();
        System.out.println(JSON.toJSONString(Metrics.globalRegistry.getMeters()));
    }

    // 使用计量器

    /**
     * 计量器名称和标签
     * 每个计量器都有自己的名称。由于不同的监控系统有自己独有的推荐命名规则，Micrometer 使用句点 . 分隔计量器名称中的不同部分，如 a.b.c。Micrometer 会负责完成所需的转换，以满足不同监控系统的需求。
     * 每个计量器在创建时都可以指定一系列标签。标签以名值对的形式出现。监控系统使用标签对数据进行过滤。除了每个计量器独有的标签之外，每个计量器注册表还可以添加通用标签。所有该注册表导出的数据都会带上这些通用标签。
     */

    /**
     * 计量器注册表的通用标签
     */
    @Test
    public void test_03() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        registry.config().commonTags("tag1", "a", "tag2", "b");
        Counter counter = registry.counter("simple", "tag3", "c");
        counter.increment();
        System.out.println(JSON.toJSONString(registry.getMeters()));
    }

    /**
     * 计数器
     * 计数器（Counter）表示的是单个的只允许增加的值。
     * 1. 通过 MeterRegistry 的 counter() 方法来创建表示计数器的 Counter 对象。
     * 2. 还可以使用 Counter.builder() 方法来创建 Counter 对象的构建器
     * Counter 所表示的计数值是 double 类型，其 increment() 方法可以指定增加的值。默认情况下增加的值是 1.0。
     * <p>
     * 如果已经有一个方法返回计数值，可以直接从该方法中创建类型为 FunctionCounter 的计数器
     */
    @Test
    public void test_04_counter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        Counter counter1 = registry.counter("simple1");
        counter1.increment();
        Counter counter2 = Counter.builder("simple2")
                .description("A simple counter")
                .tag("tag1", "a")
                .register(registry);
        counter2.increment();
        System.out.println(JSON.toJSONString(registry.getMeters()));
    }

    @Test
    public void test_05_functionCounter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        List<Tag> tags = new ArrayList<>();
        Integer a = 2;
        FunctionCounter functionCounter = registry.more().counter("function1", tags, a, Integer::intValue);

        FunctionCounter functionCounter2 = FunctionCounter.builder("function2", a, Integer::intValue)
                .description("A function counter")
                .tags(tags)
                .register(registry);

        System.out.println(JSON.toJSONString(registry.getMeters()));
    }

    /**
     * 计量仪
     * 计量仪（Gauge）表示的是单个的变化的值。与计数器的不同之处在于，计量仪的值并不总是增加的
     * 1. Gauge 对象可以从计量器注册表中创建
     * 2. 可以使用 Gauge.builder() 方法返回的构造器来创建
     */

    /**
     * <pre>
     * gauge() 方法创建的是记录任意 Number 对象的值，gaugeCollectionSize() 方法记录集合的大小，gaugeMapSize() 方法记录 Map 的大小
     * 这 3 个方法返回的并不是 Gauge 对象，而是被记录的对象。这是由于 Gauge 对象一旦被创建，就不能手动对其中的值进行修改。
     * 在每次取样时，Gauge 会返回当前值。正因为如此，得到一个 Gauge 对象，除了进行测试之外，没有其他的意义。
     * </pre>
     */
    @Test
    public void test_06_gauge() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        AtomicInteger value = registry.gauge("gauge1", new AtomicInteger(0));
        value.set(1);

        ArrayList<String> list = registry.gaugeCollectionSize("list.size", Collections.emptyList(), new ArrayList<>());
        list.add("a");

        HashMap<String, String> map = registry.gaugeMapSize("map.size", Collections.emptyList(), new HashMap<>());
        map.put("a", "b");

        Integer obj = 1;
        Gauge.builder("value", obj, Integer::intValue)
                .description("a simple gauge")
                .tags("tags1", "a")
                .register(registry);
        System.out.println(JSON.toJSONString(registry.getMeters()));
    }

    /**
     * <pre>
     * 计时器
     * 计时器（Timer）通常用来记录事件的持续时间。计时器会记录两类数据：事件的数量和总的持续时间。
     * 在使用计时器之后，就不再需要单独创建一个计数器。
     * 1. 计时器可以从注册表中创建
     * 2. 使用 Timer.builder() 方法返回的构建器来创建
     * <p>
     * Timer 提供了不同的方式来记录持续时间
     * 1. 第一种方式是使用 record() 方法来记录 Runnable 和 Callable 对象的运行时间
     * 2. 第二种方式是使用 Timer.Sample 来保存计时状态
     * </pre>
     */
    @Test
    public void test_07_timer_record() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();

        Timer timer = registry.timer("simple");
        // 记录一次运行
        timer.record(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 手动计入一个值
        timer.record(150, TimeUnit.MILLISECONDS);
        TimeUnit unit = TimeUnit.MILLISECONDS;

        assertEquals(2, timer.count());
        assertThat(timer.max(unit), closeTo(290, 305));  // 精确值 应为 300， 但会存在极少的误差
        assertThat(timer.totalTime(unit), closeTo(440, 460));

        System.out.printf("count:%s, total: %s, max: %s, \n", timer.count(), timer.totalTime(unit), timer.max(unit));
    }

    // sample.stop(registry.timer("sample"));  的实现其实就是上面的：timer.record(durationNs, TimeUnit.NANOSECONDS);
    @Test
    public void test_08_timer_sample() throws InterruptedException {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            Timer.Sample sample = Timer.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // sample 统计完毕后，计入 timer
            long nanoSec = sample.stop(registry.timer("sample"));
            System.out.println(TimeUnit.NANOSECONDS.toMillis(nanoSec));
            latch.countDown();
        }).start();
        latch.await();
    }

    /**
     * 如果一个任务的耗时很长，直接使用 Timer 并不是一个好的选择，因为 Timer 只有在任务完成之后才会记录时间。
     * 更好的选择是使用 LongTaskTimer。LongTaskTimer 可以在任务进行中记录已经耗费的时间，它通过注册表的 more().longTaskTimer() 来创建
     */
    @Test
    public void test_09_timer_longTask() throws InterruptedException, IllegalAccessException {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        CountDownLatch latch = new CountDownLatch(1);

        LongTaskTimer longTaskTimer = registry.more().longTaskTimer("long");
        new Thread(() -> {
            longTaskTimer.record(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }).start();

        // 通过反射 拿到 任务数： DefaultLongTaskTimer 中 nextTask 默认是 AtomicLong 自增的
        // 这是内部未公开的属性，新的版本中可能实现上会改掉
        Field field = ReflectionUtils.findField(longTaskTimer.getClass(), "nextTask");
        field.setAccessible(true);
        AtomicLong count = (AtomicLong) field.get(longTaskTimer);

        LongTaskTimer.Sample sample = longTaskTimer.start();

        // sample
        for (int i = 0; i < 5; i++) {
            // 任务完成后，task 会被移除， total 的计算依赖于 task, tasks map ，值为 task 的开始事件
            System.out.printf("activeTasks: %s, total: %s, count: %s \n", longTaskTimer.activeTasks(), longTaskTimer.duration(TimeUnit.MILLISECONDS), count.get());
            Thread.sleep(1000);
            sample.stop();
        }
        latch.await();
    }

    /**
     * Distribution summary
     * 用来记录事件的分布情况。计时器本质上也是一种分布概要。表示分布概要的类 DistributionSummary
     * 1. 可以从注册表中创建，
     * 2。 也可以使用 DistributionSummary.builder() 提供的构建器来创建。
     * <p>
     * 分布概要根据每个事件所对应的值，把事件分配到对应的桶（bucket）中。Micrometer 默认的桶的值从 1 到最大的 long 值。
     * 可以通过 minimumExpectedValue 和 maximumExpectedValue 来控制值的范围。
     * 如果事件所对应的值较小，可以通过 scale 来设置一个值来对数值进行放大。
     * 与分布概要密切相关的是直方图和百分比（percentile）。
     * <p>
     * 大多数时候，我们并不关注具体的数值，而是数值的分布区间。
     * <p>
     * 比如在查看 HTTP 服务响应时间的性能指标时，通常关注是的几个重要的百分比，如 50%，75%和 90%等。
     * 所关注的是对于这些百分比数量的请求都在多少时间内完成。Micrometer 提供了两种不同的方式来处理百分比。
     * 1. 对于 Prometheus 这样本身提供了对百分比支持的监控系统，Micrometer 直接发送收集的直方图数据，由监控系统完成计算。
     * 2. 对于其他不支持百分比的系统，Micrometer 会进行计算，并把百分比结果发送到监控系统。
     */

    @Test
    public void test_10_timer_DistributionSummary (){
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        DistributionSummary summary = DistributionSummary.builder("simple")
                .description("simple distribution summary")
                .minimumExpectedValue(1L)
                .maximumExpectedValue(10L)
                .publishPercentiles(0.5, 0.75, 0.9)
                .register(registry);

        summary.record(1);
        summary.record(1.3);
        summary.record(2.4);
        summary.record(3.5);
        summary.record(4.1);
        System.out.println(summary.takeSnapshot());
    }

    private static class MyConfig implements SimpleConfig {
        @Override
        public String prefix() {
            return "my";
        }

        @Override
        public String get(String key) {
            return null;
        }
    }
}
