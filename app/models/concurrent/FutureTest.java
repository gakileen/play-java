package models.concurrent;

import common.utils.Log;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by acmac on 2016/11/28.
 */
public class FutureTest {
    private static final String TAG = FutureTest.class.getSimpleName();



    public static void testCompose() {
        Function<Integer, Integer> times2 = e -> e * 2;
        Function<Integer, Integer> squared = e -> e * e;


        Log.i(TAG, "r1", times2.compose(squared).apply(4));

        Log.i(TAG, "r1", times2.andThen(squared).apply(4));
    }


    public static void testBiFunction(String[] args) {

        BiFunction<String, List<Article>, List<Article>> byAuthor =
                (name, articles) -> articles.stream()
                        .filter(a -> a.getAuthor().equals(name))
                        .collect(Collectors.toList());

        BiFunction<String, List<Article>, List<Article>> byTag =
                (tag, articles) -> articles.stream()
                        .filter(a -> a.getTags().contains(tag))
                        .collect(Collectors.toList());


        Function<List<Article>, List<Article>> sortByDate =
                articles -> articles.stream()
                        .sorted((x, y) -> y.published().compareTo(x.published()))
                        .collect(Collectors.toList());

        Function<List<Article>, Optional<Article>> first =
                a -> a.stream().findFirst();


        Function<List<Article>, Optional<Article>> newest =
                first.compose(sortByDate);

        BiFunction<String, List<Article>, Optional<Article>> newestByAuthor =
                byAuthor.andThen(newest);

        BiFunction<String, List<Article>, List<Article>> byAuthorSorted =
                byAuthor.andThen(sortByDate);

        BiFunction<String, List<Article>, Optional<Article>> newestByTag =
                byTag.andThen(newest);
    }

    public static void testFuture() throws ExecutionException, InterruptedException {
        Log.i(TAG, "start");
        long t1 = System.currentTimeMillis();

        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "f1", Thread.currentThread().getName());
            return "1";
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "f2", Thread.currentThread().getName());
            return "2";
        });

        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "f3", Thread.currentThread().getName());
            return "3";
        });

        CompletableFuture<String> f123 = f1.thenCombine(f2, (a, b) -> a + b)
                .thenCombine(f3, (a, b) -> a + b);


        Log.i(TAG, "get", f123.get());

        Log.i(TAG, "used", System.currentTimeMillis() - t1);
        Log.i(TAG, "end");


    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testFuture();
    }
}

class Article {
    String getAuthor() {return null;}
    String getTags() {return null;}
    Integer published() {return 1;}
}