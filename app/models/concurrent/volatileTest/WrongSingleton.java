package models.concurrent.volatileTest;

/**
 * Created by ac on 2017/3/2.
 */
public class WrongSingleton {

    private static volatile WrongSingleton _instance = null;

    private WrongSingleton() {}

    public static WrongSingleton getInstance() {

        if (_instance == null) {
            _instance = new WrongSingleton();
            System.out.println("--initialized once.");
        }

        return _instance;
    }

    public static class LoopTest implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                getInstance();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new LoopTest());
        Thread t2 = new Thread(new LoopTest());
        Thread t3 = new Thread(new LoopTest());
        Thread t4 = new Thread(new LoopTest());
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        while (t1.isAlive() || t2.isAlive() || t3.isAlive()|| t4.isAlive()) {

        }
    }

}
