package ch.twidev.invodb.driver.redis.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import redis.clients.jedis.Jedis;

public class RedisLRUCache<K> extends RedisCacheStrategy<K> {

    public RedisLRUCache(Jedis jedis, String cacheKey, int capacity) {
        super(jedis, cacheKey, capacity);
    }

    @Override
    public void onPut(K key) {
        this.jedis.zadd(cacheKey + ":accessOrder", System.nanoTime(), CacheDriver.serialize(key));
    }

    @Override
    public void onGet(K key) {
        this.jedis.zadd(cacheKey + ":accessOrder", System.nanoTime(), CacheDriver.serialize(key));
    }

    @Override
    public void onRemove(K key) {
        jedis.zrem(cacheKey + ":accessOrder", CacheDriver.serialize(key));
    }

    @Override
    public void evictIfNecessary() {
        if (jedis.hlen(cacheKey) > capacity) {
            String eldestKey = jedis.zrange(cacheKey + ":accessOrder", 0, 0).iterator().next();
            jedis.hdel(cacheKey, eldestKey);
            jedis.zrem(cacheKey + ":accessOrder", eldestKey);
        }
    }
}
