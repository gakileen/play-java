package models.interview;

import java.util.Scanner;

public class Qps {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int maxQps = Integer.valueOf(in.nextLine());
        final String[] rtList = in.nextLine().split(",");
        final int requestNum = Integer.valueOf(in.nextLine());
        final int threadNum = Integer.valueOf(in.nextLine());
        System.out.println(doneTime(maxQps, rtList, requestNum, threadNum));
    }

    public static long doneTime(int maxQps, String[] rtList, int requestNum, int threadNum) {
        int qpsSum = 0;
        for (String rtString: rtList) {
            int singleMaxQps = threadNum * 1000 / Integer.valueOf(rtString);
            if (singleMaxQps > maxQps) {
                qpsSum += maxQps;
            } else {
                qpsSum += singleMaxQps;
            }
        }

        return requestNum / qpsSum * 1000;
    }

}
