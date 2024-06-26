package ch.twidev.invodb.driver.redis.cache;

import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

public abstract class RedisCacheStrategy<K> implements EvictionPolicy<K, RedissonClient> {

    protected final RedissonClient redissonClient;
    protected final String cacheKey;
    protected final int capacity;

    public RedisCacheStrategy(RedissonClient redissonClient, String cacheKey, int capacity) {
        this.redissonClient = redissonClient;
        this.cacheKey = cacheKey;
        this.capacity = capacity;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
