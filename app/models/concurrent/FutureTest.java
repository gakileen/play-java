package models.concurrent;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import common.utils.Log;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by acmac on 2016/12/21.
 */
@Singleton
public class FutureTest {

	private static final String TAG = FutureTest.class.getSimpleName();

	@Inject
	private ActorSystem actorSystem;

	public void test1() {

		Set<Thread> threadSet = new CopyOnWriteArraySet<>();

		for (int i = 0; i < 100000; i++) {
			CompletableFuture.supplyAsync(() -> {
				Thread thread = Thread.currentThread();
				threadSet.add(thread);
				return null;
			});
		}

		Log.i(TAG, "test1", "threadSet一共有" + threadSet.size() + "个线程");
		Log.i(TAG, "test1", threadSet);
	}

	public void test2() {

		Set<Thread> threadSet = new CopyOnWriteArraySet<>();

		for (int i = 0; i < 100000; i++) {
			CompletableFuture.supplyAsync(() -> {
				Thread thread = Thread.currentThread();
				threadSet.add(thread);
				return null;
			}, actorSystem.dispatcher());
		}

		Log.i(TAG, "test2", "threadSet一共有" + threadSet.size() + "个线程");
		Log.i(TAG, "test2", threadSet);
	}




	public void test3() {
		Set<Thread> threadSet = new CopyOnWriteArraySet<>();

		for (int i = 0; i < 100000; i++) {
			CompletableFuture<Integer> future = new CompletableFuture<>();
			future.complete(i);

			future.thenApply(value -> {
				Thread thread = Thread.currentThread();
				threadSet.add(thread);
				return null;
			});
		}

		Log.i(TAG, "test3", "threadSet一共有" + threadSet.size() + "个线程");
		Log.i(TAG, "test3", threadSet);
	}

	public void test4() {
		Set<Thread> threadSet = new CopyOnWriteArraySet<>();

		for (int i = 0; i < 100000; i++) {
			CompletableFuture<Integer> future = new CompletableFuture<>();
			future.complete(i);

			future.thenApplyAsync(value -> {
				Thread thread = Thread.currentThread();
				threadSet.add(thread);
				return null;
			});
		}

		Log.i(TAG, "test4", "threadSet一共有" + threadSet.size() + "个线程");
		Log.i(TAG, "test4", threadSet);
	}

	public void test5() {

		System.out.println("-------------" + ForkJoinPool.getCommonPoolParallelism());

		CompletableFuture<Integer> future = new CompletableFuture<>();
		future.complete(1);

		future.thenApply(value -> {
			System.out.println("thenApply       " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApplyAsync(value -> {
			System.out.println("thenApplyAsync  " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApply(value -> {
			System.out.println("thenApply       " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApplyAsync(value -> {
			System.out.println("thenApplyAsync  " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApply(value -> {
			System.out.println("thenApply       " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApplyAsync(value -> {
			System.out.println("thenApplyAsync  " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApply(value -> {
			System.out.println("thenApply       " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApplyAsync(value -> {
			System.out.println("thenApplyAsync  " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApply(value -> {
			System.out.println("thenApply       " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		}).thenApplyAsync(value -> {
			System.out.println("thenApplyAsync  " + value + "  " + Thread.currentThread().getName());
			return value + 1;
		});
	}
}
