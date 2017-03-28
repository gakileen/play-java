package models.algorithm.recursion;

/**
 * Created by ac on 2017/3/7.
 */
public class Factorial {

    public static int factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return factorial(n -1) * n;
        }
    }

    public static void main(String[] args) {
        for (int n = 0; n < 20; n++) {
            System.out.printf("factorial of %d is: %d\n", n, factorial(n));
        }
    }

}
