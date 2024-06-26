package ch.twidev.invodb.driver.redis;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.driver.redis.cache.RedisLRUCache;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisCacheDriver implements CacheDriver<RedissonClient> {

    private final RedissonClient redisson;

    public RedisCacheDriver(String redisHost, int redisPort, String password) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort);

        if (password != null && !password.isEmpty()) {
            config.useSingleServer().setPassword(password);
        }

        this.redisson = Redisson.create(config);
    }

    @Override
    public <K> void put(String path, K key, byte[] value) {
        redisson.getMap(path).put(CacheDriver.serialize(key), value);
    }

    @Override
    public <K> byte[] get(String path, K key) {
        return (byte[]) redisson.getMap(path).get(CacheDriver.serialize(key));
    }

    @Override
    public <K> void remove(String path, K key) {
        redisson.getMap(path).remove(CacheDriver.serialize(key));
    }

    @Override
    public <K> boolean has(String path, K key) {
        return redisson.getMap(path).containsKey(CacheDriver.serialize(key));
    }

    @Override
    public void cleanup(String path) {
        redisson.getMap(path).delete();
        redisson.getScoredSortedSet(path + ":frequency").delete();
        redisson.getScoredSortedSet(path + ":accessOrder").delete();
    }

    @Override
    public RedissonClient getConn() {
        return this.redisson;
    }

    @Override
    public <K> EvictionPolicy<K, RedissonClient> getEvictionPolicy(CachingStrategy cachingStrategy, String cacheKey, int capacity) {
        return switch (cachingStrategy) {
            case LRU -> new RedisLRUCache<>(redisson, cacheKey, capacity);
            default -> throw new IllegalStateException("Unexpected eviction policy: " + cachingStrategy);
        };
    }
}