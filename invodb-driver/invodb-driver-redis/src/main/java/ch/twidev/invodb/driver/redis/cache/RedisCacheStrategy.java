package ch.twidev.invodb.driver.redis.cache;

import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import redis.clients.jedis.Jedis;

public abstract class RedisCacheStrategy<K> implements EvictionPolicy<K, Jedis> {

    protected final Jedis jedis;
    protected final String cacheKey;
    protected final int capacity;

    public RedisCacheStrategy(Jedis jedis, String cacheKey, int capacity) {
        this.jedis = jedis;
        this.cacheKey = cacheKey;
        this.capacity = capacity;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
