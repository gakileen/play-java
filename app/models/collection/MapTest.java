package models.collection;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ac on 2017/3/6.
 */
public class MapTest {

    public static void hashMapTest() {
        HashMap<KeyDO, Integer> m = new HashMap<>();

        for (int i = 0; i < 200; i++) {
            m.put(new KeyDO(i), i);

            if (i % 10 == 0) {
                System.out.println("111");
            }

        }

        System.out.println("11111");
    }


    public static void concurrentHashMapTest() {
        ConcurrentHashMap<KeyDO, Integer> m = new ConcurrentHashMap<>();

        for (int i = 0; i < 200; i++) {
            m.put(new KeyDO(i), i);

            if (i == 6 || i == 11) {
                System.out.println("111");
            }

        }

        System.out.println("11111");
    }

    public static void main(String[] args) {
        hashMapTest();
//        concurrentHashMapTest();
    }

    static class KeyDO {
        int key;
        String a;



        KeyDO(int key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "KeyDO{" +
                    "key=" + key +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KeyDO keyDO = (KeyDO) o;

            return key == keyDO.key;
        }

        @Override
        public int hashCode() {
/*            int result = key;
            result = 31 * result + a.hashCode();
            return result;*/
            return 1;
        }


    }

}


