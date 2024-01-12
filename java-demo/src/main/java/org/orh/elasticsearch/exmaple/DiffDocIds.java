package org.orh.elasticsearch.exmaple;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import cn.hutool.core.io.FileUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.orh.basic.LogConfUtil;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DiffDocIds {

    private static final String CLUSTER_OLD = "TODO OLD";
    private static final String CLUSTER_NEW = "TODO NEW";

    // 新老集群用的索引别名相同，所以这里只用了一个索引名称
    private static final String INDEX = "biz_user_list";

    static RestHighLevelClient clientOld;
    static RestHighLevelClient clientNew;

    static {
        // 指定用户名密码 - 这里新老的用户名、密码相同 所以就用了一个 credentialsProvider
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("TODO", "TODO"));
        // 创建 RestHighLevelClient 对象
        clientOld = new RestHighLevelClient(
                RestClient.builder(new HttpHost(CLUSTER_OLD, 9200)).setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
        );
        clientNew = new RestHighLevelClient(
                RestClient.builder(new HttpHost(CLUSTER_NEW, 9200)).setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                )
        );
    }


    public static void main(String[] args) throws IOException {
        // 配置下日志输出级别，避免输出太多debug信息
        LogConfUtil.configLogBack();

        RangeQueryBuilder filterCondition = QueryBuilders.rangeQuery("create_time").gte("2023-12-17 00:00:00");

        // 创建查询条件
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                // 指定时间范围
                .filter(filterCondition);

        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));

        // 查询老集群
        SearchRequest searchRequestOld = new SearchRequest(INDEX);
        searchRequestOld.source().query(queryBuilder);

        int size = 5000;
        searchRequestOld.source().size(size);
        searchRequestOld.source().fetchSource("_id", null);
        searchRequestOld.scroll(scroll);

        // ① 首次查询，没有 scrollId
        SearchResponse searchResponseOld = clientOld.search(searchRequestOld, RequestOptions.DEFAULT);

        String scrollId = searchResponseOld.getScrollId();

        // 初始化变量
        List<String> diffIds = new ArrayList<>();
        int total = 0;

        SearchHits searchHits = searchResponseOld.getHits();
        SearchHit[] hits = searchHits.getHits();
        boolean first = true;


        while (hits != null && hits.length > 0) {
            StopWatch stopWatch = new StopWatch();
            if (!first) {
                // ③ 非首次查询，指定scrollId
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
                searchScrollRequest.scroll(scroll);

                // 查询老集群
                stopWatch.start("query_old");
                searchResponseOld = clientOld.scroll(searchScrollRequest, RequestOptions.DEFAULT);
                stopWatch.stop();

                hits = searchResponseOld.getHits().getHits();
                scrollId = searchResponseOld.getScrollId();
            }
            first = false;
            List<String> oldIds = new ArrayList<>();
            if (hits != null) {
                oldIds = Arrays.stream(hits).map(SearchHit::getId).collect(Collectors.toList());
            }

            if (CollectionUtils.isEmpty(oldIds)) {
                ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                clearScrollRequest.addScrollId(scrollId);
                clientOld.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);

                System.out.println("query over.");
                break;
            }

            total += oldIds.size();

            // ② 根据老索引ids 查询新索引
            QueryBuilder newQueryBuilder = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.termsQuery("_id", oldIds))
                    // 指定时间范围
                    .filter(filterCondition);
            SearchRequest searchRequestNew = new SearchRequest(INDEX);
            searchRequestNew.source().size(size);
            searchRequestNew.source().query(newQueryBuilder);
            searchRequestNew.source().fetchSource("_id", null);


            // 查询新集群
            stopWatch.start("query_new");
            SearchResponse searchResponseNew = clientNew.search(searchRequestNew, RequestOptions.DEFAULT);
            stopWatch.stop();

            if (searchResponseNew.getHits() != null && searchResponseNew.getHits().getHits() != null) {
                Set<String> newIds = Arrays.stream(searchResponseNew.getHits().getHits()).map(SearchHit::getId).collect(Collectors.toSet());
                for (String oldId : oldIds) {
                    if (!newIds.contains(oldId)) {
                        diffIds.add(oldId);
                    }
                }
            } else {
                diffIds.addAll(oldIds);
            }
            if (CollectionUtils.isNotEmpty(diffIds)) {
                FileUtil.writeLines(diffIds, "c:\\tmp\\user_diff_ids.txt", StandardCharsets.UTF_8, true);
                diffIds.clear();
            }
            System.out.println(
                    "scrollId: " + scrollId + "\n" +
                            "acc total: " + total + "\n" +
                            stopWatch.prettyPrint());
        }
        // 关闭连接
        clientOld.close();
        clientNew.close();
    }

}