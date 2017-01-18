package controllers;

import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.utils.Log;
import helpers.WishesDto;
import models.Test;
import models.concurrent.FutureTest;
import models.concurrent.ParallelStreamTest;
import models.dubbo.consumer.DubboConsumerHelper;
import models.dubbo.provider.DubboProviderHelper;
import models.mysql.GroupMember;
import models.redisCluster.RedisCluster;
import models.ws.WsTest;
import org.bson.Document;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

/**
 * Created by acmac on 2016/05/05.
 */
@Singleton
public class TestController extends Controller {

    private static final String TAG = TestController.class.getSimpleName();

    @Inject
    FormFactory formFactory;


    @Inject Test test;

    @Inject
    WsTest wsTest;

	@Inject
	FutureTest futureTest;

    public Result testValidData() {

        System.out.println("testValidData start");

        Form<WishesDto> form = formFactory.form(WishesDto.class).bindFromRequest();

        if (form.hasErrors()) {
            System.out.println("valid error");
            return badRequest();
        }

        WishesDto wishesDto = form.get();
        if (wishesDto != null) {
            System.out.println(wishesDto.getAnon());
            System.out.println(wishesDto.getBonus());
            System.out.println(wishesDto.getTag());
            for (String s : wishesDto.getKeyword()) {
                System.out.println(s);
            }

        } else {
            System.out.println("no data");
        }

        System.out.println("testValidData end");

        return ok();
    }

    public Result mongo() {
        MongoClient conn = new MongoClient("127.0.0.1", 27017);
        MongoDatabase db = conn.getDatabase("dm");
        MongoCollection<Document> wishesUserColl = db.getCollection("wishes.wishes_user");


        Document doc = wishesUserColl.find().first();
        System.out.println(doc);

        return ok();
    }

    public Result schedule() {

        System.out.println("TestController schedule start...");

        System.out.println(test);

        test.schedule();

        System.out.println("TestController schedule end");

        return ok();
    }


    public Result excel() throws Exception {

        ByteArrayOutputStream os = Test.exportExcel();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        os.close();

        long now = System.currentTimeMillis();
        response().setHeader("Content-Disposition","attachment;filename=" + now + ".xls");

        return ok(is).as("application/x-xls");
    }

    public Result dubbo() {
        DubboProviderHelper.init();
        DubboConsumerHelper.init();

        DubboConsumerHelper.getMsgService().sendCmdMsg(null, null, null, null, null, null, 0, 0);
        DubboConsumerHelper.getMsgService().sendTxtMsg(null, null, null, null, null, 0, 0);

        return ok();
    }

    public Result redisSet(String key, String value) {
        RedisCluster.set(key, value);
        return ok();
    }

    public Result redisGet(String key) {
        String value = RedisCluster.get(key);
        return ok(value);
    }

    public Result search(String question) {
        Log.i(TAG, "search", question);
        return ok(Test.searchFirstResult(question)).as("text/html;charset=utf-8");
    }

    public CompletionStage<Result> ws() {
        return wsTest.get().thenApply(jsonNode -> {
            Log.i(TAG, Thread.currentThread().getName());
            return ok(jsonNode);
        });
    }

    public Result mysql(long gid, String uid) {
		Set<Long> set = GroupMember.getGidsByUid(uid);

		System.out.println(set);

		return ok();
	}

	public Result mysql_rand(long gid, String uid) {
		List<Long> set = GroupMember.rand(uid);

		System.out.println(set);

		return ok();
	}

	public Result parallelStream() throws Exception {
		ParallelStreamTest.main(null);
		return ok();
	}

	public Result future() throws Exception {
		Log.i(TAG, "future5");

		futureTest.test5();
		return ok();
	}

	public Result json() {
		return ok(Json.newArray());
	}

	public Result sleeping() throws Exception {
		Thread.sleep(10000);
		return ok();
	}

	public Result sleeping2() throws Exception {
		int a = 1;

		while(a > 0) {
			Thread.sleep(10000);
		}
		return ok();
	}

	public Result sleeping3() throws Exception {

		new Thread(() -> {
			int a = 1;

			while(a > 0) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		return ok();
	}

	public Result sleeping4() throws Exception {

		Thread t = new Thread(() -> {
			int a = 1;

			while(a > 0) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		t.start();

		t.join();

		return ok();
	}

}
