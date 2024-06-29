package ch.twidev.invodb.driver.redis.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

public abstract class RedisCacheStrategy<K> implements EvictionPolicy<K, RedissonClient> {

    protected final RedissonClient redissonClient;
    protected final int capacity;
    protected boolean map;

    protected String cacheKey;

    public RedisCacheStrategy(RedissonClient redissonClient, String cacheKey, int capacity, boolean map) {
        this.redissonClient = redissonClient;
        this.cacheKey = cacheKey;
        this.capacity = capacity;
        this.map = map;
    }

    @Override
    public void initEviction(boolean isMap) {
        this.map = isMap;
    }

    public boolean isMap() {
        return map;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    @Override
    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    @Override
    public String serialize(K key) {
        if(isMap()) return key.toString();

        return CacheDriver.serialize(key);
    }
}
