package models.collection;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ac on 2017/3/1.
 */
public class QueueTest {

    public static void arrayDeque1() throws NoSuchFieldException, IllegalAccessException {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        for (int i = 0; i < 15; i++)  {
            ad.add(i);
        }

        ad.remove();
        ad.remove();

        ad.add(15);
        ad.add(16);
        ad.add(17);

        Field field = ArrayDeque.class.getDeclaredField("elements");
        field.setAccessible(true);
        Object[] o = (Object[]) field.get(ad);

        Arrays.asList(o).forEach(System.out::println);

        System.out.println("=====");
    }

    public static void arrayDeque2() throws NoSuchFieldException, IllegalAccessException {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        for (int i = 0; i < 5; i++)  {
            ad.add(i);
        }

        ad.addFirst(5);

        ad.remove();

        ad.removeLast();

        Field field = ArrayDeque.class.getDeclaredField("elements");
        field.setAccessible(true);
        Object[] o = (Object[]) field.get(ad);

        Arrays.asList(o).forEach(System.out::println);

        System.out.println("=====");
    }

    public static void main(String[] args) throws Exception {
        arrayDeque2();
    }

}
