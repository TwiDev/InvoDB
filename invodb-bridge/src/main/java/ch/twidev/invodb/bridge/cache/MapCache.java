package ch.twidev.invodb.bridge.cache;

import java.util.Map;

public interface MapCache<K>{
    <V> void put(K key, V value);
    <V> V get(K key, Class<V> v);

    default String getString(K key) {
        return this.get(key,String.class);
    }

    default int getInt(K key) {
        return this.get(key,Integer.class);
    }

    void clear();

    boolean has(K key);

    void remove(K key);

    Map<K, Object> getMap();

    String getKeyname();
}
