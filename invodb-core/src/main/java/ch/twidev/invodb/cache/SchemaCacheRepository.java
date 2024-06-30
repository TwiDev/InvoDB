package ch.twidev.invodb.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.mapper.InvoSchema;

import java.util.concurrent.CompletionStage;

public class SchemaCacheRepository<Driver> implements EvictionPolicy<String, Driver> {

    private final EvictionPolicy<String, Driver> evictionPolicy;

    public SchemaCacheRepository(EvictionPolicy<String, Driver> evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
    }

    public SchemaCacheRepository(CacheDriver<Driver> cacheDriver, String collection, CachingStrategy cachingStrategy, int capacity) {
        this.evictionPolicy = cacheDriver.getEvictionPolicy(cachingStrategy, collection, capacity, true);
    }

    public SchemaCacheRepository(CacheDriver<Driver> cacheDriver, String collection) {
        this.evictionPolicy = cacheDriver.getEvictionPolicy(CachingStrategy.NONE, collection, Integer.MAX_VALUE, true);
    }


    @Override
    public void initEviction(boolean isMap) {
        evictionPolicy.initEviction(isMap);
    }

    @Override
    public void onPut(String key) {
        evictionPolicy.onPut(key);
    }

    @Override
    public void onGet(String key) {
        evictionPolicy.onGet(key);
    }

    @Override
    public void onRemove(String key) {
        evictionPolicy.onRemove(key);
    }

    @Override
    public void evictIfNecessary() {
        evictionPolicy.evictIfNecessary();
    }

    @Override
    public void clear() {
        evictionPolicy.clear();
    }

    @Override
    public CompletionStage<Boolean> onPutAsync(String key) {
        return evictionPolicy.onPutAsync(key);
    }

    @Override
    public CompletionStage<Boolean> onGetAsync(String key) {
        return evictionPolicy.onGetAsync(key);
    }

    @Override
    public CompletionStage<Boolean> onRemoveAsync(String key) {
        return evictionPolicy.onRemoveAsync(key);
    }

    @Override
    public CompletionStage<Boolean> evictIfNecessaryAsync() {
        return evictionPolicy.evictIfNecessaryAsync();
    }

    @Override
    public String getCacheKey() {
        return evictionPolicy.getCacheKey();
    }

    @Override
    public String serialize(String key) {
        return evictionPolicy.serialize(key);
    }
}
