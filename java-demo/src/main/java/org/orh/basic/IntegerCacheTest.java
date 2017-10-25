package org.orh.basic;


/**
 *  分别不使用和使用 -XX:AutoBoxCacheMax=1000  来设置 缓存的最大值， 比较输出结果 - 关于此配置的其他使用限制请自行搜索
 */
public class IntegerCacheTest {
    public static void main(String[] args) throws InterruptedException {
        Integer i = 1000; Integer j = 1000;
        System.out.println(i == j); // 这里也验证了那道 题目 i == j 可能返回 true、也可能返回 false

        // 假设 0 - 1000 是我们应用中高频使用的数字
        long start = System.currentTimeMillis();
        Integer[] arr = new Integer[10_000_000]; // 初始1千万

        System.out.printf("malloc elapsed time: %d\n", (System.currentTimeMillis() - start)); // 结果：这个不会有差别 一般是 10ms左右

        start = System.currentTimeMillis();
        for (i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 1001); // 此处 会默认调用 Integer.valueOf(i)，结果：此处用时差别会比较大：new 会耗时 3000ms 左右，而在cache内耗时只要300ms, 相差会有10倍
        }

        System.out.printf("array set value elapsed time: %d", (System.currentTimeMillis() - start));

        Thread.sleep(Integer.MAX_VALUE); // 进入sleep ，便于观察 内存

        // 开启 jvisualvm/jconsole/ 去观察下内存
        // 测试 场景：1 千万的 Integer数组，将每个设值为[0, 1000]的随机整数，对比初始时间、各代空间占比

        // 总的对比结果（前者代表默认，后者代表--XX:AutoBoxCacheMax=1000）：
        //  1. 申请1千万数组空间时间无差异，都是在 10ms 左右
        //  2. 遍历设值初始值，前者会隐式的使用 new，耗时 3000ms左右，后者会隐式命中 cache，耗时 300ms, 相差10倍
        //  3. 空间使用对比
        //  3.1 前者 eden 空间 使用比后者 eden 空间 大出 1倍
        //  3.2 前者 old 空间使用了 126M，后者没有使用 old空间
    }
}
