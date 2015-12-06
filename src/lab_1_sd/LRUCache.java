package lab_1_sd;

import java.util.LinkedHashMap;

/**
 *
 * @author nelson
 */
public class LRUCache {

    int size;
    LinkedHashMap<String, String> cache;
    
    public LRUCache(int size) {
        this.size = size;
        this.cache = new LinkedHashMap<>();
    }
    
    // Devolvemos la respuesta a una query
    public String getEntryFromCache(String query) {
        String result = cache.get(query);
        if(result != null) {
            cache.remove(query);
            cache.put(query, result);
        }
        return result;
    }
    
    // Eliminamos una query
    public void removeEntryFromCache(String query) {
        String result = cache.get(query);
        if(result != null) {
            cache.remove(query);
        }
    }
    
    // Actualizamos la respuesta a una query
    public String updateAnswerFromCache(String query, String answer) {
        String result = cache.get(query);
        if(result != null) {
            cache.remove(query);
            cache.put(query, answer);
        }
        return result;
    }

    // Agregamos una query al cache, o bien la actualizamos
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
    
    // Mostramos todas las llaves del cache
    public void print() {
        System.out.println("===== My LRU Cache =====");
        System.out.println("| " + String.join(" | ", cache.keySet()) + " | ");
        System.out.println("========================");
    }
    
    // Mostramos todas las values del cache
    public void printAns() {
        System.out.println("===== My LRU Cache Answers =====");
        System.out.println("| " + String.join(" | ", cache.values()) + " | ");
        System.out.println("========================");
    }

}
