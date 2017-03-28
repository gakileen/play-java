package models.algorithm.sort;

/**
 * Created by ac on 2017/3/10.
 */
public class HeapSort {
    public static void printArray(int[] array){
        System.out.print("{");
        for (int i : array) {
            System.out.print(i+" ");
        }
        System.out.println("}");
    }
    public static void exchange(int[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void heapSort(int[] array){
        if(array ==null || array.length<=1){
            return;
        }
        //建堆
        buildMaxHeap(array);
        //排序，将最大值一个一个放在后面
        for (int i=array.length-1; i>0 ; i--) {
            exchange(array,0,i);
            //恢复堆
            maxHeap(array,i,0);
        }
    }
    public static void buildMaxHeap(int[] array){
        if (array == null || array.length<=1) {
            return;
        }
        int half = array.length/2;
        for (int i=half-1;i>=0 ;i-- ) {
            maxHeap(array,array.length,i);
        }
    }
    public static void maxHeap(int[] array,int heapSize,int index){
        int left = index * 2 + 1;
        int right = index * 2 +2;
        int largest = index;
        if (left<heapSize && array[left]>array[index]) {
            largest = left;
        }
        if (right<heapSize && array[right]>array[largest]) {
            largest = right;
        }
        if (index != largest) {
            exchange(array,index, largest);
            maxHeap(array, heapSize,largest);
        }
    }


    public static void main1(String[] args) {
        int array[] ={9,18,7,6,15,4,3,12,1,10,-1,-21,-3,0};
        System.out.println("Before heap:");
        printArray(array);
        heapSort(array);
        System.out.println("After heap sort:");
        printArray(array);
    }

    public static void main(String[] args) {
        int[] array ={9,18,7,6,15,4,3,12,1,10,-1,-21,-3,0};


        buildMaxHeap(array);

        printArray(array);
    }
}
