package ch.twidev.invodb.bridge.cache;

public interface CacheDriver<Driver> extends EvictionProvider<Driver>{
    <K, V> void put(K key, V value);
    <K, V> V get(K key);
    <K> void remove(K key);
    void cleanup();

    Driver getConn();
}