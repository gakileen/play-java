package models.algorithm;

import java.util.ArrayList;
import java.util.Collections;

public class DynamicProgramming {

    public static void main(String[] args) {
//        getLCS("bab", "caba");

        getLIS();
    }

    public static void getLIS() {
        int[] nums = {1, 3, 6, 4, 5, 2, 3};

        ArrayList<Integer> dp = new ArrayList<>();

        for (int item: nums) {
            if (dp.size() == 0 || dp.get(dp.size() - 1) < item) {
                dp.add(item);
                System.out.println(item);
            } else {
                int i = Collections.binarySearch(dp, item);
                System.out.println("i: " + i + " item: " + item);
                dp.set(i < 0 ? -i -1 : i, item);
            }
        }

        System.out.println("====" + dp.size());
        System.out.println(dp);
    }

    public static void getMaxSubArray() {

        int[] a = {-3, -1, 3, -2, 5, -5};

        int maxsum = a[0];
        int maxhere = a[0];

        for (int i = 1; i < a.length; i++) {
            if (maxhere <= 0) {
                maxhere = a[i];
            } else {
                maxhere += a[i];
            }

            if (maxsum < maxhere) {
                maxsum = maxhere;
            }
        }


        System.out.println(maxsum);
    }

    public static void getLCS(String a, String b) {
        if (a == null || b == null || a.length() == 0 || b.length() == 0) {
            System.out.println("");
        }

        int[][] dp = new int[a.length()][b.length()];

        int endHere = 0;
        int maxLen = 0;

        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = a.charAt(i) == b.charAt(j) ? 1 : 0;
                } else {
                    dp[i][j] = a.charAt(i) == b.charAt(j) ? dp[i-1][j-1] + 1 : 0;
                }

                if (dp[i][j] > maxLen) {
                    maxLen = dp[i][j];
                    endHere = i;
                }
            }
        }

        String result = a.substring(endHere - maxLen + 1, endHere + 1);
        System.out.println(result);

    }
}
