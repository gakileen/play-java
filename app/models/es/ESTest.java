package models.es;

import com.google.inject.Guice;
import com.google.inject.Injector;
import common.utils.ApplicationHelper;
import common.utils.Log;
import org.bson.Document;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by acmac on 2016/11/18.
 */
@Singleton
public class ESTest {

    private static final String TAG = ESTest.class.getSimpleName();

    @Inject ESClient esClient;

    public void get() {
        GetResponse response = esClient.getTransportClient().prepareGet("nickname", "shockings", "1001546443").get();

        System.out.println("-----------------");
        System.out.println(response.getIndex());
        System.out.println(response.getType());
        System.out.println(response.getVersion());
        System.out.println(response.getId());
        System.out.println(response.getSourceAsString());
    }

    public void multiGet() {
        MultiGetResponse multiGetItemResponses = esClient.getTransportClient().prepareMultiGet()
                .add("nickname_0.1", "shockings", "1001546443")
                .add("omnivideo_0.1", "shockings", "56456bfa5b9256394e977ea3")
                .add("profile_0.1", "files", "AVh1JropJibRFBbovTyK")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                System.out.println("-----------------");
                System.out.println(response.getIndex());
                System.out.println(response.getType());
                System.out.println(response.getVersion());
                System.out.println(response.getId());
                System.out.println(response.getSourceAsString());
            }
        }
    }

    public void search() {
        SearchResponse searchResponse = esClient.getTransportClient().prepareSearch("nickname_0.1")
                .setTypes("shockings")
                .setQuery(QueryBuilders.matchQuery("nick", "美女"))
                .setFrom(0).setSize(10)
                .execute()
                .actionGet();

        searchResponse.getHits().forEach(searchHit -> {
            System.out.println("-----------------");
            System.out.println(searchHit.getIndex());
            System.out.println(searchHit.getType());
            System.out.println(searchHit.getVersion());
            System.out.println(searchHit.getId());
            System.out.println(searchHit.getSourceAsString());
            System.out.println(searchHit.getScore());
        });
    }



    public void searchNickName() throws ExecutionException, InterruptedException {
        CompletableFuture<SearchHits> completionFuture = new CompletableFuture<>();

        ListenableActionFuture<SearchResponse> future = esClient.getTransportClient().prepareSearch("nickname_0.1")
                .setTypes("shockings")
                .setQuery(QueryBuilders.matchQuery("nick", "美女"))
                .setFrom(0).setSize(10)
                .execute();

        future.addListener(new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                Log.i(TAG, "in ListenableActionFuture onResponse", Thread.currentThread().getName());
                completionFuture.complete(searchResponse.getHits());
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        CompletableFuture<Optional<Document>> result =  completionFuture.thenApply(searchHits -> {
            Log.i(TAG, "in CompletableFuture thenAccept", Thread.currentThread().getName());
            List<Document> list = new ArrayList<>();

            searchHits.forEach(searchHit -> {
                Document doc = new Document();
                doc.append("index", searchHit.getIndex());
                doc.append("type", searchHit.getType());
                doc.append("version", searchHit.getVersion());
                doc.append("id", searchHit.getId());
                doc.append("source", searchHit.getSourceAsString());
                doc.append("score", searchHit.getScore());
                list.add(doc);
            });

            Document doc = new Document("data", list);

            return Optional.ofNullable(doc);
        });


        String json = result.get().map(document -> ApplicationHelper.toJson(document)).orElseGet(() -> "");
        Log.i(TAG, "result", Thread.currentThread().getName(), json);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Injector injector = Guice.createInjector();
        ESTest test = injector.getInstance(ESTest.class);

//        test.get();

//        test.multiGet();

//        test.search();

        test.searchNickName();



        // close
        test.esClient.close();
    }

}
