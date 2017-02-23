package models.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ac on 2017/1/25.
 */
public class UnsafeTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        int NUM_OF_THREADS = 1000;
        int NUM_OF_INCREMENTS = 100000;
        ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);


        // creating instance of specific counter
        Counter counter = null;
//        counter = new StupidCounter();
//        counter = new SyncCounter();
//        counter = new LockCounter();
//        counter = new AtomicCounter();
        counter = new CASCounter();


        long before = System.currentTimeMillis();
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            service.submit(new CounterClient(counter, NUM_OF_INCREMENTS));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        long after = System.currentTimeMillis();
        System.out.println("Counter result: " + counter.getCounter());
        System.out.println("Time passed in ms:" + (after - before));

    }

}

class CounterClient implements Runnable {

    private Counter c;
    private int m;

    CounterClient(Counter c, int m) {
        this.c = c;
        this.m = m;
    }


    @Override
    public void run() {
        for (int i = 0; i < m; i++) {
            c.increment();
        }
    }
}


interface Counter {
    void increment();
    long getCounter();
}

class StupidCounter implements Counter {
    private long counter = 0;

    @Override
    public void increment() {
        counter++;
    }

    @Override
    public long getCounter() {
        return counter;
    }
}

class SyncCounter implements Counter {
    private long counter = 0;

    @Override
    public synchronized void increment() {
        counter++;
    }

    @Override
    public long getCounter() {
        return counter;
    }
}


class LockCounter implements Counter {
    private long counter = 0;
    private ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();

    @Override
    public void increment() {
        writeLock.lock();
        counter++;
        writeLock.unlock();
    }

    @Override
    public long getCounter() {
        return counter;
    }
}

class AtomicCounter implements Counter {
    private AtomicLong counter = new AtomicLong(0);

    @Override
    public void increment() {
        counter.getAndIncrement();
    }

    @Override
    public long getCounter() {
        return counter.get();
    }
}


class CASCounter implements Counter {
    private volatile long counter = 0;
    private Unsafe unsafe;
    private long offset;

    CASCounter() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            offset = unsafe.objectFieldOffset(CASCounter.class.getDeclaredField("counter"));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void increment() {
        long before = counter;
        while (! unsafe.compareAndSwapLong(this, offset, before, before+1)) {
            before = counter;
        }
    }

    @Override
    public long getCounter() {
        return counter;
    }
}