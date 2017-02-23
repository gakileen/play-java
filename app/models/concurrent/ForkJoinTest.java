package models.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by ac on 2017/1/22.
 */
public class ForkJoinTest {

    private static class ForkJoinTask extends RecursiveTask<Integer> {
        private int first;
        private int last;
        public ForkJoinTask(int first, int last) {
            this.first = first;
            this.last = last;
        }
        protected Integer compute() {
            int subCount;
            if (last - first < 10) {
                subCount = 0;
                for (int i = first; i <= last; i++) {
                    if (i % 7 == 0) {
                        subCount ++;
                    }
                }
            }
            else {
                int mid = (first + last) >>> 1;
                ForkJoinTask left = new ForkJoinTask(first, mid);
                left.fork();
                ForkJoinTask right = new ForkJoinTask(mid + 1, last);
                right.fork();
                subCount = left.join();
                subCount += right.join();
            }
            return subCount;
        }
    }
    public static void main(String[] args) {

        int n = new ForkJoinPool().invoke(new ForkJoinTask(0, 9999999));
        System.out.println("Found " + n + " values");
    }
}