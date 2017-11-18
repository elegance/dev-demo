package org.orh;

import org.orh.util.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

/**
 * 测试连接 tzb360 websocket
 */
public class WebsocketClientTest {

    public static void main(String[] args) {
        Runner.runExample(WebsocketsClient.class);
    }

    class WebsocketsClient extends AbstractVerticle {

        @Override
        public void start() throws Exception {
            HttpClient client = vertx.createHttpClient(new HttpClientOptions().setSsl(true).setTrustAll(true)); // 设置 SSL 连接，
            MultiMap headers = new CaseInsensitiveHeaders();
            headers.add("Cookie", "TZB_SESSIONID=e58b01b8db4d4901b7d8c1e7b9547e52"); // 设置登录的 Cookie 请求头

            client.websocket(443, "www.tzb360.com", "/tzb-api/webSocket/pollingData", headers, websocket -> { // 注意：☆☆☆  当连接为 SSL时， 需要填写 443端口
                websocket.handler(data -> {
                    System.out.println("Received data " + data.toString("UTF-8"));
                    client.close();
                });

                // 发送文本信息
                websocket.writeTextMessage(
                        "{\"strategyid\":\"769\",\"marketypeid\":3,\"stockId\":\"\",\"userid\":\"\",\"accountid\":\"\",\"pushType\":\"position\"}");
            });
        }
    }
}
