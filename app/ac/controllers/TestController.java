package ac.controllers;

import ac.Test;
import ac.WishesDto;
import ac.dubbo.consumer.DubboConsumerHelper;
import ac.dubbo.provider.DubboProviderHelper;
import ac.redisCluster.RedisCluster;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.utils.Log;
import org.bson.Document;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by acmac on 2016/05/05.
 */
public class TestController extends Controller {

    private static final String TAG = TestController.class.getSimpleName();

    @Inject
    FormFactory formFactory;


    @Inject Test test;

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

}
