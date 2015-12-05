package tcpserver;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author nelson
 */
public class LRUCache {

    int size;
    LinkedHashMap<String, String> cache;

    public String getEntryFromCache(String query) {
        String result = cache.get(query);
        if(result != null) {
            cache.remove(query);
            cache.put(query, result);
        }
        return result;
    }
    
    public String updateAnswerFromCache(String query, String answer) {
        String result = cache.get(query);
        if(result != null) {
            cache.remove(query);
            cache.put(query, answer);
        }
        return result;
    }

    public LRUCache(int size) {
        this.size = size;
        this.cache = new LinkedHashMap<>();
    }

    public void addEntryToCache(String query, String answer) {
        if (cache.containsKey(query)) { // HIT
            // Bring to front
            cache.remove(query);
            cache.put(query, answer);
        } else { // MISS
            if(cache.size() == this.size) {
                String first_element = cache.entrySet().iterator().next().getKey();
                System.out.println("Removiendo: '" + first_element + "'");
                cache.remove(first_element);
            }
            cache.put(query, answer);
        }
    }

    public void print() {
        System.out.println("===== My LRU Cache =====");
        System.out.println("| " + String.join(" | ", cache.keySet()) + " | ");
        System.out.println("========================");
    }

    public void printAns() {
        System.out.println("===== My LRU Cache Answers =====");
        System.out.println("| " + String.join(" | ", cache.values()) + " | ");
        System.out.println("========================");
    }

}
