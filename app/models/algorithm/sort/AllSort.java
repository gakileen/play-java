package models.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;

public class AllSort {

    private static final int[] NUMBERS = {49, 38, 65, 97, 76, 13, 27, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18};

    private static void insertSort(int[] array) {

        for (int i = 1; i < array.length; i ++) {
            int temp = array[i];
            int j = i - 1;

            for (; j >= 0 && array[j] > temp; j--) {
                array[j + 1] = array[j];
            }

            array[j + 1] = temp;
        }
    }

    private static void shellSort(int[] array) {
        int gap = 1;
        while (gap < array.length / 3) {
            gap = gap * 3 + 1;
        }

        for (; gap > 0; gap /= 3) {
            for (int i = gap; i < array.length; i++) {
                int temp = array[i];
                int j = i - gap;

                for (; j >= 0 && array[j] > temp; j -= gap) {
                    array[j + gap] = array[j];
                }

                array[j + gap] = temp;
            }
        }
    }


    private static void selectSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int temp = array[i];
            int positon = i;

            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < temp) {
                    temp = array[j];
                    positon = j;
                }
            }

            array[positon] = array[i];
            array[i] = temp;
        }
    }


    private static void bubbleSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    private static void quickSort(int[] array) {
        _quickSort(array, 0, array.length - 1);
    }

    private static void _quickSort(int[] array, int low, int high) {
        if (low < high) {
            int middle = getMiddle(array, low, high);
            _quickSort(array, low, middle - 1);
            _quickSort(array, middle + 1, high);
        }
    }

    private static int getMiddle(int[] array, int low, int high) {
        int temp = array[low];

        while (low < high) {
            while (low < high && array[high] > temp) {
                high--;
            }

            array[low] = array[high];

            while (low < high && array[low] < temp) {
                low++;
            }

            array[high] = array[low];
        }

        array[low] = temp;

        return low;
    }

    private static void mergeSort(int[] array) {
        _mergeSort(array, 0, array.length - 1);
    }

    private static void _mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            _mergeSort(array, left, center);
            _mergeSort(array, center + 1, right);
            merge(array, left, center, right);
        }
    }

    private static void merge(int[] array, int left, int center, int right) {
        int i = left;
        int j = center + 1;

        int[] tempArray = new int[array.length];
        int tempIndex = left;

        while (i <= center && j <= right) {
            if (array[i] < array[j]) {
                tempArray[tempIndex++] = array[i++];
            } else {
                tempArray[tempIndex++] = array[j++];
            }
        }

        while (i <= center) {
            tempArray[tempIndex++] = array[i++];
        }

        while (j <= right) {
            tempArray[tempIndex++] = array[j++];
        }

        for (int k = left; k <= right; k++) {
            array[k] = tempArray[k];
        }
    }

    private static void radixSort(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        int time = 0;
        while (max > 0) {
            max = max / 10;
            time++;
        }


        ArrayList<ArrayList<Integer>> queue = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            queue.add(new ArrayList<>());
        }

        for (int i = 0; i < time; i++){
            for (int n : array) {
                int x = n % (int) Math.pow(10, i + 1) / (int) Math.pow(10, i);
                queue.get(x).add(n);
            }

            int count = 0;
            for (int k = 0; k < 10; k++) {
                while (queue.get(k).size() > 0) {
                    array[count++] = queue.get(k).remove(0);
                }
            }
        }
    }

    private static void heapSort(int[] array) {
        buildMaxHeap(array);

        for (int i = array.length - 1; i>0; i--) {
            exchange(array, 0, i);
            maxHeap(array, i, 0);
        }

    }

    private static void buildMaxHeap(int[] array) {
        int half = array.length / 2;
        for (int i = half - 1; i >= 0; i--) {
            maxHeap(array, array.length, i);
        }
    }

    private static void maxHeap(int[] array, int heapSize, int index) {
        int left = index * 2 + 1;
        int right = index * 2 + 2;
        int largest = index;

        if (left < heapSize && array[left] > array[index]) {
            largest = left;
        }

        if (right < heapSize && array[right] > array[index]) {
            largest = right;
        }

        if (index != largest) {
            exchange(array, index, largest);
            maxHeap(array, heapSize, largest);
        }
    }

    private static void exchange(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


    public static void main(String[] args) {
//        insertSort(NUMBERS);

//        shellSort(NUMBERS);

//        selectSort(NUMBERS);

        heapSort(NUMBERS);

//        bubbleSort(NUMBERS);

//        quickSort(NUMBERS);

//        mergeSort(NUMBERS);

//        radixSort(NUMBERS);



        System.out.println(Arrays.toString(NUMBERS));
    }
}
