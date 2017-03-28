package models.concurrent;


import java.util.Arrays;

/**
 * Created by ac on 2017/3/8.
 */
public class DoubleCheckedLocking {

    private volatile Helper helper = null;

    public Helper getHelper() {
        if (helper == null) {
            synchronized(this) {
                if (helper == null) {
                    helper = new Helper();
                }
            }
        }
        return helper;
    }

    public static void main(String[] args) {
        DoubleCheckedLocking dcl = new DoubleCheckedLocking();

        for (int i = 0; i < 3; i++) {
            new Thread(() -> dcl.getHelper().work(), i + "").start();
        }
    }

    class Helper {
        private String value;

        private Helper() {
            try {
                int a = 2;
                while (a > 1) {
                    a = 0;
                }
                value = "ok";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void work() {
            System.out.println(Thread.currentThread().getName() + " work " + value);
        }
    }

}



