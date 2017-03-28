package models.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ac on 2017/3/21.
 */
public class Intersection {

    /**
     * 二路归并
     */
    public static void method1(int[] arr1, int[] arr2) {
        int i = 0, j = 0;

        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] < arr2[j]) {
                i++;
            } else if (arr1[i] > arr2[j]) {
                j++;
            } else {
                System.out.println(arr1[i]);
                i++;
                j++;
            }
        }
    }

    /**
     * 数组1存Map，遍历数组2
     */
    public static void method2(int[] arr1, int[] arr2) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int a1 : arr1) {
            map.put(a1, 1);
        }

        for (int a2 : arr2) {
            if (map.containsKey(a2)) {
                System.out.println(a2);
            }
        }
    }



    public static void method3(int[] arr1, int[] arr2) {

    }


    public static void main(String[] args) {
        int[] arr1 = new int[] {-2, -1, 0, 1, 3, 5, 7, 8};
        int[] arr2 = new int[] {-5, -3, -2, 1, 2, 5, 6, 10};

//        method1(arr1, arr2);
        method2(arr1, arr2);
    }

}
