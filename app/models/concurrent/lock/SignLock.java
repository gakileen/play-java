package models.concurrent.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by ac on 2017/1/24.
 */
public class SignLock {

    private AtomicReference<Thread> sign = new AtomicReference<>();

    public void lock() {
        Thread t = Thread.currentThread();
        while (! sign.compareAndSet(null, t)) {}
    }

    public void unlock() {
        Thread t = Thread.currentThread();
        sign.compareAndSet(t, null);
    }


}
