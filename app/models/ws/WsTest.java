package models.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.utils.Log;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import javax.inject.Singleton;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by acmac on 2016/11/23.
 */
@Singleton
public class WsTest implements Closeable {

    private static final String TAG = WsTest.class.getSimpleName();

//    @Inject
//    WSClient ws;

    WSClient ws = WS.newClient(-1);

    Executor executor = Executors.newFixedThreadPool(2);

    public CompletionStage<JsonNode> get() {

        WSRequest request = ws.url("http://210.73.213.236/v1/recommends/news");
        WSRequest complexRequset = request.setHeader("X-UserId", "9990444")
                .setRequestTimeout(1000)
                .setQueryParameter("isTop", "1");

        Log.i(TAG, Thread.currentThread().getName(), "start...");

        CompletionStage<WSResponse> responesePromise = complexRequset.get();

        CompletionStage<JsonNode> result = responesePromise.thenApplyAsync(wsResponse -> {
            Log.i(TAG, Thread.currentThread().getName(), "thenApply");
            return wsResponse.asJson();
        }, executor);

        Log.i(TAG, Thread.currentThread().getName(), "end!!!");


        return result;
    }

    @Override
    public void close() {
        try {
            ws.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        WsTest test = injector.getInstance(WsTest.class);

        test.get().thenApplyAsync(jsonNode -> {
            Log.i(TAG, Thread.currentThread().getName(), jsonNode.toString());
            return null;
        }).thenAccept(var -> test.close());
    }
}
