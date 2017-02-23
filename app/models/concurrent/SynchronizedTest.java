package models.concurrent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ac on 2017/1/23.
 */
public class SynchronizedTest {

    private Map<Integer, Integer> map = new HashMap<>();

    private final Integer l1 = 0;
    private final Integer l2 = 0;


    public Integer get(Integer k) {
        synchronized (l1) {
            Integer v = map.get(k);
            System.out.println("111111: " + k + " " + v);
            return v;
        }
    }

    public void put(Integer k, Integer v, long sleepMillis) {
        synchronized (l2) {
            map.put(k, v);
            System.out.println("222222: " + k + " " + v);

            sleep(sleepMillis);
        }
    }

    public static void main(String[] args) {

        SynchronizedTest test = new SynchronizedTest();
        test.put(1, 1, 0);
        test.put(2, 2, 0);
        test.put(3, 3, 0);

        new Thread(() -> {
            for (int i = 4; i < 10; i++) {
                test.put(i, i, 1500);
            }
        }).start();

        test.sleep(1000);

        new Thread(() -> {
            for (int i = 1; i < 10; i++) {
                test.get(i);
            }
        }).start();

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
