package ac;


import common.dbconns.MongoConn;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by wan on 16/5/8.
 */
public class MongoDB {
    public static MongoConn KY_HOME = new MongoConn("ky_shockings");
    public static MongoConn KY_DM = new MongoConn("ky_dm");
    public static MongoConn BJ_WISHES = new MongoConn("bj_wishes");

    public static MongoDatabase db = KY_HOME.getDBByName("zl");

    public static MongoCollection<Document> orderColl = db.getCollection("orders");
}
