package ch.twidev.invodb.driver.redis;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.driver.redis.cache.RedisLRUCache;
import redis.clients.jedis.Jedis;

public class RedisCacheDriver implements CacheDriver<Jedis> {

    private final Jedis jedis;

    public RedisCacheDriver(String redisHost, int redisPort, String password) {
        this.jedis = new Jedis(redisHost, redisPort);

        if(password != null)
            this.jedis.auth(password);
    }

    @Override
    public <K> void put(String path, K key, byte[] value) {
        jedis.hset(path.getBytes(), serialize(key).getBytes(), value);
    }

    @Override
    public <K>  byte[] get(String path, K key) {
        return jedis.hget(path.getBytes(), serialize(key).getBytes());
    }

    @Override
    public <K>  void remove(String path, K key) {
        jedis.hdel(path.getBytes(), serialize(key).getBytes());
    }

    @Override
    public <K>  boolean has(String path, K key) {
        return jedis.hexists(path.getBytes(), serialize(key).getBytes());
    }

    @Override
    public void cleanup(String path) {
        jedis.del(path);
        jedis.del(path + ":frequency");
        jedis.del(path + ":accessOrder");
    }

    @Override
    public Jedis getConn() {
        return this.jedis;
    }

    @Override
    public <K> EvictionPolicy<K, Jedis> getEvictionPolicy(CachingStrategy cachingStrategy, String cacheKey, int capacity) {
        return switch (cachingStrategy) {
            case LRU -> new RedisLRUCache<>(jedis, cacheKey, capacity);
            default -> throw new IllegalStateException("Unexpected eviction policy: " + cachingStrategy);
        };
    }
}
