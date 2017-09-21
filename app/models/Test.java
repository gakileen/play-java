package models;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.sun.tools.javac.util.Convert;
import common.utils.ApplicationHelper;
import common.utils.Log;
import dbconns.MongoDB;
import io.advantageous.boon.core.Sys;
import jobs.TestJob;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.asynchttpclient.ws.WebSocket;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import sun.misc.Unsafe;
import sun.misc.VM;
import sun.reflect.Reflection;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by acmac on 2016/05/03.
 */
public class Test {

    private static String TAG = "LauncherTest";

    @Inject ActorSystem actorSystem;

	private static Random random = new Random();

    public static void json() throws Exception {
        String str = "{\"keyword\": [\"纸牌屋\",\"美剧\"],\"tag\": \"美剧\",\"bonus\": 50,\"anon\": 1}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(str);

        System.out.println(json.findPath("tag").asText());

        List list = new ArrayList();
        for (JsonNode jn : json.findPath("keyword")) {
            list.add(jn.asText());
        }
        System.out.println((list));
    }

    public static void trie() {
        Trie trie = new Trie();
        trie.addKeyword("毛泽东");
        trie.addKeyword("邓小平");
        trie.addKeyword("江泽民");
        trie.addKeyword("胡锦涛");
        trie.addKeyword("习近平");

        String sentence = "毛泽东邓小平胡锦涛毛泽东";
        Collection<Emit> emits = trie.parseText(sentence);
        System.out.println(emits.size());
        emits.forEach(System.out::println);
    }

    public static void sort() {

        Document sort1 = new Document("u@", -1).append("time", 1);

        List<Document> list = new ArrayList<Document>();
        list.add(new Document("name", "d1").append("time", 12227).append("u@", 100026));
        list.add(new Document("name", "d2").append("time", 12223).append("u@", 100025));
        list.add(new Document("name", "d3").append("time", 12226).append("u@", 100025));
        list.add(new Document("name", "d4").append("time", 12225).append("u@", 100021));
        list.add(new Document("name", "d5").append("time", 12229).append("u@", 100025));
        list.add(new Document("name", "d6").append("time", 12220).append("u@", 100025));



        Collections.sort(list, ((o1, o2) -> {
            int result = 0;
            Iterator<String> iterator = sort1.keySet().iterator();

            while (result==0 && iterator.hasNext()) {
                String key = iterator.next();
                int sort = sort1.getInteger(key, -1);

                result = sort > 0 ? Long.valueOf(o1.get(key).toString()).compareTo(Long.valueOf(o2.get(key).toString()))
                        : Long.valueOf(o2.get(key).toString()).compareTo(Long.valueOf(o1.get(key).toString()));
            }

            return result;
        }));

        for (Document doc : list) {
            System.out.println(doc);
        }

    }

    public static void uuid() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        System.out.println(uuid);
    }

    public void schedule() {

        System.out.println("LauncherTest schedule start...");

        actorSystem.scheduler().schedule(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(10, TimeUnit.SECONDS),
                () -> System.out.println(System.currentTimeMillis()),
                actorSystem.dispatcher()
        ) ;

        System.out.println("LauncherTest schedule end");
    }

    public static void quartz() {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();


            JobDetail job = newJob(TestJob.class).withIdentity("TestJob").build();

/*            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever())
                    .build();*/

            Trigger trigger = newTrigger().withIdentity("TestJob").withSchedule(cronSchedule("* * 17 * * ?")).build();

            scheduler.scheduleJob(job, trigger);

            scheduler.start();

//            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    public static void httpClient() throws Exception {

        long t1 = System.currentTimeMillis();

        String uid = null;
        String token = "000000b33497ae90c39196d049d4b66e";

        String json = Request.Get(TokenSystem.URL + token)
                .addHeader(TokenSystem.AUTH_KEY, TokenSystem.AUTH_VALUE)
                .connectTimeout(TokenSystem.CONNECT_TIMEOUT)
                .socketTimeout(TokenSystem.SOCKET_TIMEOUT)
                .execute().returnContent().asString();

        if (StringUtils.isNotBlank(json)) {
            uid = Document.parse(json).getString("uid");
        }

        long t2 = System.currentTimeMillis();

//        System.out.println(uid);
        System.out.println(t2 - t1);
    }

    interface TokenSystem {
        String URL = "http://api.dewmobile.net/v3/users/finduserid?token=";
        String AUTH_KEY = "Authorization";
        String AUTH_VALUE = "B62D8AE190A5C4CD01542BE54C9F153D";
        int CONNECT_TIMEOUT = 1000;
        int SOCKET_TIMEOUT = 1000;
    }


    public static ByteArrayOutputStream exportExcel() throws Exception {
        Workbook wb = new XSSFWorkbook();
        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("话费充值订单");
        sheet.setDefaultColumnWidth(20);


        CellStyle defaultStyle = wb.createCellStyle();
        defaultStyle.setAlignment(CellStyle.ALIGN_LEFT);
        defaultStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

        Row title = sheet.createRow(0);
        createCell(wb, title, defaultStyle, 0).setCellValue("任务状态");
        createCell(wb, title, defaultStyle, 1).setCellValue("跟进人员");
        createCell(wb, title, defaultStyle, 2).setCellValue("提交时间");
        createCell(wb, title, defaultStyle, 3).setCellValue("用户ID");
        createCell(wb, title, defaultStyle, 4).setCellValue("金额");
        createCell(wb, title, defaultStyle, 5).setCellValue("手机号");
        createCell(wb, title, defaultStyle, 6).setCellValue("订单状态");
        createCell(wb, title, defaultStyle, 7).setCellValue("操作时间");

        final int[] i = {1};

        MongoDB.orderColl.find().forEach((Block<Document>) obj -> {
            int mission = obj.getInteger(OrderConstant.MISSION);
            String missionStr = null;
            switch (mission) {
                case 1:
                    missionStr = "未开始";
                    break;

                case 2:
                    missionStr = "进行中";
                    break;

                case 3:
                    missionStr = "已完成";
                    break;

                default:
                    missionStr = "未知";
            }

            int state = obj.getInteger(OrderConstant.STATE);
            String stateStr = null;
            switch (state) {
                case 1:
                    stateStr = "未充值";
                    break;

                case 2:
                    stateStr = "已充值";
                    break;

                default:
                    stateStr = "未知";
            }


            Row data = sheet.createRow(i[0]++);
            createCell(wb, data, defaultStyle, 0).setCellValue(missionStr);
            createCell(wb, data, defaultStyle, 1).setCellValue(obj.getString(OrderConstant.BY));
            createCell(wb, data, dateStyle, 2).setCellValue(new Date(obj.getLong(OrderConstant.CA)));
            createCell(wb, data, defaultStyle, 3).setCellValue(obj.getString(OrderConstant.UID));
            createCell(wb, data, defaultStyle, 4).setCellValue(obj.getInteger(OrderConstant.AMOUNT));
            createCell(wb, data, defaultStyle, 5).setCellValue(obj.getString(OrderConstant.MOBILE));
            createCell(wb, data, defaultStyle, 6).setCellValue(stateStr);
            createCell(wb, data, dateStyle, 7).setCellValue(new Date(obj.getLong(OrderConstant.UA)));
        });


/*        FileOutputStream fileOut = new FileOutputStream("话费充值订单.xls");
        wb.write(fileOut);
        fileOut.close();
        return null;*/

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        return os;
    }

    private static Cell createCell(Workbook wb, Row row, CellStyle cellStyle, int column) {
        Cell cell = row.createCell(column);
        cell.setCellStyle(cellStyle);

        return cell;
    }

    interface OrderConstant {
        String UID = "uid";
        String MISSION = "mission";
        String STATE = "state";
        String AMOUNT = "amount";
        String CA = "c@";
        String UA = "u@";
        String BY = "by";
        String MOBILE = "mobile";
    }


    public static long getZeroTime(int dayAfter) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, dayAfter);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime().getTime();
    }

    public static String getObjectId() {
        return ObjectId.get().toString();
    }

    private static MessageDigest md5=null;

    public static String string2MD5(String inStr){

        if(null == md5) {
            try {
                md5=MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        byte[] byteArray = null;
        try {
            byteArray = inStr.getBytes("UTF-8");

            for (byte b : byteArray) {
                System.out.print(b + " ");
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void parallel() {

        long t1 = System.currentTimeMillis();

        List<String> vendorList = Arrays.asList("v1", "v2", "v3");

        List<String> list = new ArrayList<String>();

        vendorList.parallelStream().forEach(s -> {
            for (int i=0; i< 3; i++) {
                list.add(s + ":" + String.valueOf(i));
            }

            System.out.println(Thread.currentThread().getName());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        list.forEach(System.out::println);

        System.out.println(System.currentTimeMillis() - t1);

    }

    public static String searchFirstResult(String question) {
        Log.i(TAG, "searchFirstResult", question);

        List<String> engines = Arrays.asList(/*"http://www.baidu.com/s?wd=",*/ "http://www.sogou.com/web?query=",
                "https://www.bing.com/search?PC=U316&FORM=CHROMN&q=");


        AtomicReference<String> result = new AtomicReference<>();

        for (String base : engines) {
            String url = base + question;
//            new Thread(() -> WS.url(url).get().thenAccept(t -> result.compareAndSet(null, t.getBody()))).start();


            new Thread(() -> {
                try {
                    result.compareAndSet(null, Request.Get(url).connectTimeout(2000).socketTimeout(2000)
                            .execute().returnContent().asString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        while (result.get() == null);

        String ret = result.get();
        Log.i(TAG, "searchFirstResult success", question);

        return ret;
    }

    public static void exception() {


    	try {
    		String s = null;

			s.hashCode();

		} catch(IndexOutOfBoundsException e) {
			System.out.println("in catch IndexOutOfBoundsException");
		} finally {
			System.out.println("in finally");
		}

	}

    static final int spread(int h) {
        return (h ^ (h >>> 16)) & 0x7fffffff;
    }

    static void temp (int concurrencyLevel) {
        int MAX_SEGMENTS = 65535;

        if (concurrencyLevel > MAX_SEGMENTS)
            concurrencyLevel = MAX_SEGMENTS;

        // Find power-of-two sizes best matching arguments
        int sshift = 0;
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ++sshift;
            ssize <<= 1;
        }
        int segmentShift = 32 - sshift;
        int segmentMask = ssize - 1;


        System.out.println(ssize);
        System.out.println(sshift);
        System.out.println(segmentShift);
        System.out.println(segmentMask);
    }


    public static String digui(int i) {

        int a = i / 10;
        int b = i % 10;

        if (a == 0) {
            return b + "";
        } else {
            return b + digui(a);
        }
    }

    public static byte[] getSaltAndIv() {
        SecureRandom sr;
        byte[] salt = new byte[8];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salt;
    }

    public  static void simpleHttp() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://127.0.0.1:9000/v2/recs/salt?SessionId=100").openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestProperty("SessionId", "2");

        connection.connect();

        InputStream inputStream = connection.getInputStream();

        byte[] bytes = new byte[8];
        inputStream.read(bytes);

        for (byte b : bytes) {
            System.out.print(b + " ");
        }

        inputStream.close();
        connection.disconnect();
    }

    private static final String FILED_ID = "_id";
    private static final String FILED_PKG = "pkg";
    private static final String FILED_ICON = "icon";
    private static final String FILED_URL = "url";
    private static final String FILED_GPURL = "gpurl";
    private static final String FILED_SIZE = "size";
    private static final String FILED_MULTI_LAN = "multi_lan";
    private static final String FILED_LAN = "lan";
    private static final String FILED_TITLE = "title";
    private static final String FILED_DESC = "desc";
    private static final String FILED_ORDERS = "orders";
    private static final String FILED_LIMIT_PID = "limit_pid";
    private static final String FILED_LIMIT_CHN = "limit_chn";
    private static final String FILED_LIMIT_CC = "limit_cc";
    private static final String FILED_CT = "ct";
    private static final String FILED_UT = "ut";

    private static boolean isValidDoc(Document d) {
        boolean result = d.containsKey(FILED_ID) && d.get(FILED_ID) != null &&
                d.containsKey(FILED_PKG) && d.get(FILED_PKG) != null &&
                d.containsKey(FILED_ICON) && d.get(FILED_ICON) != null &&
                d.containsKey(FILED_URL) && d.get(FILED_URL) != null &&
                d.containsKey(FILED_GPURL) && d.get(FILED_GPURL) != null &&
                d.containsKey(FILED_SIZE) && d.get(FILED_SIZE) != null &&
                d.containsKey(FILED_MULTI_LAN) && d.get(FILED_MULTI_LAN) != null &&
                d.containsKey(FILED_ORDERS) && d.get(FILED_ORDERS) != null &&
                d.containsKey(FILED_LIMIT_PID) && d.get(FILED_LIMIT_PID) != null &&
                d.containsKey(FILED_LIMIT_CHN) && d.get(FILED_LIMIT_CHN) != null &&
                d.containsKey(FILED_LIMIT_CC) && d.get(FILED_LIMIT_CC) != null;

        if (result) {
            try {
                if (((List<Integer>) d.get(FILED_LIMIT_PID)).stream().map(Integer.class::isInstance).filter(a -> !a).count() > 0) {
                    return false;
                }

                if (((List<String>) d.get(FILED_LIMIT_CHN)).stream().map(String.class::isInstance).filter(a -> !a).count() > 0) {
                    return false;
                }

                if (((List<String>) d.get(FILED_LIMIT_CC)).stream().map(String.class::isInstance).filter(a -> !a).count() > 0) {
                    return false;
                }

                List<Document> multiLanList = (List<Document>) d.get(FILED_MULTI_LAN);
                if (ApplicationHelper.isEmpty(multiLanList)) {
                    return false;
                }

                for (Document a : multiLanList) {
                    boolean r = a.containsKey(FILED_LAN) && a.get(FILED_LAN) != null &&
                            a.containsKey(FILED_TITLE) && a.get(FILED_TITLE) != null &&
                            a.containsKey(FILED_DESC) && a.get(FILED_DESC) != null;

                    result = result && r;
                }

                return result;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("------start------");

        String a = "123321";
        System.out.println(isTargetStr(a));

        System.out.println("------success------");
    }

    public static boolean isTargetStr(String str) {
        if (str != null && str.length() > 0) {
            int len = str.length();
            for (int i = 0; i <= len / 2; i++) {
                if (str.charAt(i) != str.charAt(len - 1 - i)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStrToByteArray(String str)
    {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++){
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }

    static String setParameter(String sql, String param) {
        return sql.replaceFirst("\\?", "'" + param + "'");
    }

    public static void concurrent() throws Exception {
        int threadCount = 3;
        int loopCount = 10;

        Thread[] ts = new Thread[threadCount];
        for (int i=0; i<threadCount; i++) {
            ts[i] = new Thread(() -> {
                for (int j=0; j<loopCount; j++) {
                    /************BUSI************/





                    /************BUSI************/
                }
            });
        }

        for (Thread t : ts) {
            t.start();
        }

        for (Thread t : ts) {
            t.join();
        }
    }



}
