package models.algorithm.recursion;

/**
 * Created by ac on 2017/3/7.
 */
public class Fibonacci {

    public static int fibonacci(int n) {
        if (n == 0 || n == 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n -2);
        }
    }

    public static void main(String[] args) {
        for (int n = 0; n < 20; n++) {
            System.out.printf("Fibonacci of %d is: %d\n", n, fibonacci(n));
        }
    }

}
