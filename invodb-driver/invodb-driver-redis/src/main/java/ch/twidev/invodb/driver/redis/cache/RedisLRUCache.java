package ch.twidev.invodb.driver.redis.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

public class RedisLRUCache<K> extends RedisCacheStrategy<K> {

    private final RMap<String, Object> cacheMap;
    private final RScoredSortedSet<String> accessOrderSet;

    public RedisLRUCache(RedissonClient redisson, String cacheKey, int capacity) {
        super(redisson, cacheKey, capacity);
        this.cacheMap = redisson.getMap(cacheKey);
        this.accessOrderSet = redisson.getScoredSortedSet(cacheKey + ":accessOrder");
    }

    @Override
    public void onPut(K key) {
        accessOrderSet.add(System.nanoTime(), CacheDriver.serialize(key));
    }

    @Override
    public void onGet(K key) {
        accessOrderSet.add(System.nanoTime(), CacheDriver.serialize(key));
    }

    @Override
    public void onRemove(K key) {
        accessOrderSet.remove(CacheDriver.serialize(key));
    }

    @Override
    public void evictIfNecessary() {
        if (cacheMap.size() > capacity) {
            String eldestKey = accessOrderSet.first();
            cacheMap.fastRemove(eldestKey);
            accessOrderSet.remove(eldestKey);
        }
    }
}
