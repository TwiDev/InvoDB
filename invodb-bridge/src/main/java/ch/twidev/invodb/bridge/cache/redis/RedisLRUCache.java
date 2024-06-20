package ch.twidev.invodb.bridge.cache.redis;

import ch.twidev.invodb.bridge.cache.EvictionPolicy;

public class RedisLRUCache<K> implements EvictionPolicy<K, Object> {

    @Override
    public void onPut(Object o, String cacheKey, K key) {

    }

    @Override
    public void onGet(Object o, String cacheKey, K key) {

    }

    @Override
    public void onRemove(Object o, String cacheKey, K key) {

    }

    @Override
    public void evictIfNecessary(Object o, String cacheKey, int capacity) {

    }
}
