package models.algorithm.sort;

import java.util.Arrays;

/**
 * Created by ac on 2017/3/10.
 */
public class MergeSort {

    public static void mergeSort(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        int middle = (left + right) / 2;
        mergeSort(array, left, middle);
        mergeSort(array, middle + 1, right);

        merge(array, left, middle, right);
    }

    public static void merge(int[] array, int left, int middle, int right) {

        int[] tempArr = new int[array.length];
        int arrindex = left;

        int arr1left = left;
        int arr2left = middle + 1;

        while (arr1left <= middle && arr2left <= right) {
            if (array[arr1left] < array[arr2left]) {
                tempArr[arrindex++] = array[arr1left++];
            } else {
                tempArr[arrindex++] = array[arr2left++];
            }
        }

        while (arr1left <= middle) {
            tempArr[arrindex++] = array[arr1left++];
        }

        while (arr2left <= right) {
            tempArr[arrindex++] = array[arr2left++];
        }

        for (int i = left; i <= right; i++) {
            array[i] = tempArr[i];
        }

    }

    public static void main(String[] args) {
        int[] arr= new int[]{9,3,6,4,7,1,0,2,5};

        mergeSort(arr, 0, arr.length - 1);

        Arrays.stream(arr).forEach(System.out::println);

    }


}
