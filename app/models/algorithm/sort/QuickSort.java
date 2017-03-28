package models.algorithm.sort;

import java.util.Arrays;

/**
 * Created by ac on 2017/3/10.
 */
public class QuickSort {

    public static void quickSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        int i = partion(arr, left, right);
        quickSort(arr, left, i -1);
        quickSort(arr, i + 1, right);
    }

    public static int partion(int[] arr, int left, int right) {
        int x = arr[left];

        int l = left;
        int r = right;

        while (l < r) {
            while (l < r && arr[r] >= x) {
                r--;
            }
            if (l < r) {
                arr[l] = arr[r];
            }
            while (l < r && arr[l] <= x) {
                l++;
            }
            if (l < r) {
                arr[r] = arr[l];
            }
        }

        arr[l] = x;

        return l;
    }


    public static void main(String[] args) {
        int arr[]={3,46,7,8,2,9,0,4,2,8,9,1};
        quickSort(arr,0,arr.length-1);
        Arrays.stream(arr).forEach(System.out::println);
    }

}
