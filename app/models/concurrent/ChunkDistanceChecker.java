package models.concurrent;

import java.util.*;

/**
 * Created by acmac on 2016/09/21.
 */
public class ChunkDistanceChecker {
    private final String[] knownWords;

    public ChunkDistanceChecker(String[] knowns) {
        knownWords = knowns;
    }

    /**
     * Build list of checkers spanning word list.
     *
     * @param words
     * @param block
     * @return checkers
     */
    public static List<ChunkDistanceChecker> buildCheckers(String[] words, int block) {
        List<ChunkDistanceChecker> checkers = new ArrayList<>();
        for (int base = 0; base < words.length; base += block) {
            int length = Math.min(block, words.length - base);
            checkers.add(new ChunkDistanceChecker(Arrays.copyOfRange(words, base, base + length)));
        }

        return checkers;
    }

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        List<String> list = new ArrayList<>();


    }

}
