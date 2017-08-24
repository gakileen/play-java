package helpers;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ac on 2017/2/27.
 */
public class NumberSystem26 {

    public static String ToNumberSystem26(long n){
        String s = "";
        while (n > 0){
            long m = n % 26;
            if (m == 0) m = 26;
            s = (char)(m + 64) + s;
            n = (n - m) / 26;
//            System.out.println(m + " - " + s);
        }
        return s;
    }

    // 将指定的26进制表示转换为自然数。映射关系：[A-Z] ->[1-26]。
    // <param name="s">26进制表示（如果无效，则返回0）。</param>
    // <returns>自然数。</returns>
    public static long FromNumberSystem26(String s){
        if (StringUtils.isBlank(s)) return 0;
        long n = 0;
        for (int i = s.length() - 1, j = 1; i >= 0; i--, j *= 26){
            char c = s.charAt(i);
            if (c < 'A' || c > 'Z') return 0;
            n += ((int)c - 64) * j;
//            System.out.println(c + " - " + n);
        }
        return n;
    }

    public static void main(String[] args){
//        System.out.println(ToNumberSystem26(2337L));
//        System.out.println(FromNumberSystem26("CKW"));


//        System.out.println(ToNumberSystem26(2156L));
//        System.out.println(FromNumberSystem26("CDX"));

    }

}
