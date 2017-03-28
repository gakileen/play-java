package models.collection;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by ac on 2017/3/7.
 */
public class ListTest {

    public static void arrayList() {
        ArrayList aList = new ArrayList();
        for (int i = 0; i < 12; i++) {
            aList.add(i + "");
        }
        System.out.println(aList);
        System.out.println(aList.indexOf("2"));
        System.out.println(Collections.binarySearch(aList, "2"));
    }

    public static void linkedList() {
        LinkedList lList = new LinkedList();
        for (int i = 1; i < 12; i++) {
            lList.add(i + "");
        }
        System.out.println(lList);

        lList.addFirst("0");
        System.out.println(lList);
        lList.addLast("2");
        System.out.println(lList);

        System.out.println("元素 2 第一次出现的位置：" + lList.indexOf("2"));
        System.out.println("元素 2 最后一次出现的位置："+ lList.lastIndexOf("2"));
        System.out.println(Collections.binarySearch(lList, "2"));
    }

    public static void vector() {
        Vector<String> v = new Vector();
        for (int i = 1; i < 12; i++) {
            v.add(i + "");
        }

        System.out.println(v);
        Collections.swap(v, 0, 4);
        System.out.println("旋转后");
        System.out.println(v);
    }

    public static void main(String[] args) {

//        arrayList();
//        linkedList();
//        vector();

        String[] ss = "a:b::c:::d::".split(":");
        System.out.println(ss.length);
        for (String s : ss) {
            System.out.println(s);
        }

    }

}
