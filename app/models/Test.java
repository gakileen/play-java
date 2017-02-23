package models;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
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
import java.net.InetAddress;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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

    public static void main(String[] args) throws Exception {
        System.out.println("------start------");

        String str = "";

        String[] ss = str.split(":", 2);

        for (String s : ss) {
            System.out.println(s);
        }



        System.out.println("------success------");
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
