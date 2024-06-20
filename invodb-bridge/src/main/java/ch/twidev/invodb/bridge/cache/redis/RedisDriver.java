package ch.twidev.invodb.bridge.cache.redis;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;

public class RedisDriver implements CacheDriver<Object> {
    @Override
    public <K, V> void put(K key, V value) {

    }

    @Override
    public <K, V> V get(K key) {
        return null;
    }

    @Override
    public <K> void remove(K key) {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public Object getConn() {
        return null;
    }

    @Override
    public <K> EvictionPolicy<K, Object> getEvictionPolicy(CachingStrategy cachingStrategy) {
        return null;
    }
}
