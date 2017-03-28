package models.algorithm.sort;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ac on 2017/3/10.
 */
public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
        root.wordEnd = false;
    }

    public void insert(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            Character c = new Character(word.charAt(i));
            if (!node.childdren.containsKey(c)) {
                node.childdren.put(c, new TrieNode());
            }
            node = node.childdren.get(c);
        }
        node.wordEnd = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        boolean found = true;
        for (int i = 0; i < word.length(); i++) {
            Character c = new Character(word.charAt(i));
            if (!node.childdren.containsKey(c)) {
                return false;
            }
            node = node.childdren.get(c);
        }
        return found && node.wordEnd;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        boolean found = true;
        for (int i = 0; i < prefix.length(); i++) {
            Character c = new Character(prefix.charAt(i));
            if (!node.childdren.containsKey(c)) {
                return false;
            }
            node = node.childdren.get(c);
        }
        return found;
    }

}

class TrieNode {
    Map<Character, TrieNode> childdren;
    boolean wordEnd;

    public TrieNode() {
        childdren = new HashMap<Character, TrieNode>();
        wordEnd = false;
    }
}