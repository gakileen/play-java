package models.sort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ac on 2017/1/19.
 */
public class UnConsecutiveSort {

    public static void main(String[] args) {

        List<Recommend> results = new ArrayList<>();

        List<Recommend> recommends = Arrays.asList(
                new Recommend("u1", "c3"),
                new Recommend("u1", "c1"),
                new Recommend("u1", "c2"),
                new Recommend("u2", "c4"),
                new Recommend("u2", "c2"),
                new Recommend("u2", "c3"),
                new Recommend("u2", "c5"),
                new Recommend("u2", "c1"),
                new Recommend("u3", "c6"),
                new Recommend("u3", "c3"),
                new Recommend("u3", "c5"),
                new Recommend("u3", "c1"),
                new Recommend("u3", "c2"),
                new Recommend("u3", "c7"),
                new Recommend("u3", "c4")
        );

//        Map<String, List<Recommend>> map = recommends.stream().collect(Collectors.groupingBy(r -> r.uid));
        Map<String, List<Recommend>> map = recommends.stream().sorted(Comparator.comparing(r -> r.content)).collect(Collectors.groupingBy(r -> r.uid));

        List<String> uids = map.entrySet().stream().sorted(Comparator.comparing(e -> -e.getValue().size()))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        int maxSize = map.get(uids.get(0)).size();

        for (int i = 0; i < maxSize; i++) {
            for (String uid : uids) {
                if (i < map.get(uid).size()) {
                    results.add(map.get(uid).get(i));
                }
            }
        }

        results.forEach(System.out::println);
    }

}

class Recommend {
    String uid;
    String content;

    Recommend(String uid, String content) {
        this.uid = uid;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Recommend{" +
                "uid='" + uid + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
