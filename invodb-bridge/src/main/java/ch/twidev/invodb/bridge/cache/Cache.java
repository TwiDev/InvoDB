package ch.twidev.invodb.bridge.cache;

public interface Cache<K, V> {

    void put(K key, V value);
    V get(K key);
    void remove(K key);

    boolean has(K key);

    String getKeyname();
}
