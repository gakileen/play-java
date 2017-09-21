package models.concurrent.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        List<String> fileList = Arrays.asList("file1", "file2");

         ThreadPoolExecutor pool = new ThreadPoolExecutor(100, 1000, -1, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));
        AtomicLong count = new AtomicLong();

        // for (String file : fileList) {
        //   pool.excute(new DealFile(file, count));
        // }


        int threadNum = 100;
        CountDownLatch cdl = new CountDownLatch(threadNum);

        DealFile[] threads = new DealFile[threadNum];
        for (int i = 0; i < threadNum; i++) {
            int n = fileList.size() / threadNum;
//            List<String> files = fileList.subList(i * n, (i + 1) * n);
            threads[i] = new DealFile(count, new ArrayList<>(), cdl);
        }

        for (Thread t: threads) {
            t.start();
        }


         for (Thread t: threads) {
           t.join();
         }

//        cdl.await();

        // 等待所有线程执行完成

        System.out.println(count.get());

    }


}

class DealFile extends Thread {

    List<String> files ;
    AtomicLong count;
    CountDownLatch cdl;

    DealFile(AtomicLong count, List<String> files, CountDownLatch cdl) {

        this.files = files;
        this.count = count;
        this.cdl = cdl;
    }

    public void run() {

//        for (String file: files) {
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(new File(file)));
//
//                String line;
//                while ((line = br.readLine()) != null) {
//                    if (isTargetStr(line)) {
//                        count.getAndIncrement();
//                    }
//                }
//
//                br.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

        for (int i = 0; i < 10000; i++) {
            count.getAndIncrement();
        }

        cdl.countDown();

    }


    public boolean isTargetStr(String str) {
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


}