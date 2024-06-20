package ch.twidev.invodb.bridge.cache;

public interface EvictionPolicy<K, Driver> {
    void onPut(Driver driver, String cacheKey, K key);
    void onGet(Driver driver, String cacheKey, K key);
    void onRemove(Driver driver, String cacheKey, K key);
    void evictIfNecessary(Driver driver, String cacheKey, int capacity);
}