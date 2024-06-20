package ch.twidev.invodb.bridge.cache.redis;

import ch.twidev.invodb.bridge.cache.EvictionPolicy;

public class RedisLRUCache<K> implements EvictionPolicy<K, RedisDriver<K, ?>> {
    @Override
    public void onPut(RedisDriver<K, ?> kRedisDriver, String cacheKey, K key) {

    }

    @Override
    public void onGet(RedisDriver<K, ?> kRedisDriver, String cacheKey, K key) {

    }

    @Override
    public void onRemove(RedisDriver<K, ?> kRedisDriver, String cacheKey, K key) {

    }

    @Override
    public void evictIfNecessary(RedisDriver<K, ?> kRedisDriver, String cacheKey, int capacity) {

    }
}
