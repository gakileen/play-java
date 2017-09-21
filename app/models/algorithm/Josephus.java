package models.algorithm;

public class Josephus {

    public static void main(String[] args) {
        int m = 41;
        int k = 3;

//        for (int i = 1; i <=m; i++) {
//            System.out.printf("第%d出环的索引为:%d\n", i, josephus(m, k, i));
//        }

        josephus2(m, k, 2);

    }


    private static int josephus(int m, int k, int i) {
        if (i == 1) {
            return (m + k - 1) % m;
        } else {
            return (josephus(m - 1, k, i - 1) + k) % m;
        }
    }


    private static void josephus2(int m, int k, int alive) {
        int[] man = new int[m];

        int pos = -1;
        int i = 0;
        int count = 1;

        while (count <= m) {
            do {
                pos = (pos + 1) % m;
                if (man[pos] == 0) {
                    i++;
                }

                if (i == k) {
                    i = 0;
                    break;
                }

            } while (true);

            man[pos] = count;

            count++;
        }

        for (int j = 0; j < man.length; j++) {
            if (man[j] >= count - alive) {
                System.out.println("不被杀的位置是->" + (j + 1));
            }
        }
    }

}
