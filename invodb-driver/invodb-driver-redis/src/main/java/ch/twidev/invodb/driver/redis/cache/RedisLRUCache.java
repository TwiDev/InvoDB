package ch.twidev.invodb.driver.redis.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletionStage;

public class RedisLRUCache<K> extends RedisCacheStrategy<K> {

    private RMap<String, Object> cacheMap;
    private RScoredSortedSet<String> accessOrderSet;

    public RedisLRUCache(RedissonClient redisson, String cacheKey, int capacity, boolean isMap) {
        super(redisson, cacheKey, capacity, isMap);

        this.cacheMap = redissonClient.getMap(cacheKey);
        this.accessOrderSet = redissonClient.getScoredSortedSet(cacheKey + ":accessOrder");
    }

    @Override
    public void onPut(K key) {
        accessOrderSet.add(System.nanoTime(), serialize(key));
    }

    @Override
    public void onGet(K key) {
        accessOrderSet.add(System.nanoTime(), serialize(key));
    }

    @Override
    public void onRemove(K key) {
        accessOrderSet.remove(serialize(key));
    }

    @Override
    public void evictIfNecessary() {
        if ((!isMap() ? cacheMap.size() : accessOrderSet.size()) > capacity) {
            String eldestKey = accessOrderSet.first();

            if(isMap()) {
                redissonClient.getMap(eldestKey).delete();
            }else {
                cacheMap.fastRemove(eldestKey);
            }
            accessOrderSet.remove(eldestKey);
        }
    }

    @Override
    public CompletionStage<Boolean> onPutAsync(K key) {
        return accessOrderSet.addAsync(System.nanoTime(), serialize(key));
    }

    @Override
    public CompletionStage<Boolean> onGetAsync(K key) {
        return accessOrderSet.addAsync(System.nanoTime(), serialize(key));
    }

    @Override
    public CompletionStage<Boolean> onRemoveAsync(K key) {
        return accessOrderSet.removeAsync(serialize(key));
    }

    @Override
    public CompletionStage<Boolean> evictIfNecessaryAsync() {
        return cacheMap.sizeAsync().thenAccept(size -> {
            if(size > capacity) {
                accessOrderSet.firstAsync().thenAccept(eldestKey -> {
                    cacheMap.fastRemoveAsync(eldestKey);
                    accessOrderSet.removeAsync(eldestKey);
                });
            }
        }).thenApply(result -> true);
    }
}
