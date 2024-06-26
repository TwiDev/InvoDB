package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.bridge.documents.ElementSet;

import java.util.concurrent.CompletionStage;

public class AsyncQueryCache<Driver> extends AsyncStreamCacheProvider<Integer, ElementSet<?>, Driver> {
    public AsyncQueryCache(CacheDriver<Driver> driver, CachingStrategy cachingStrategy, String keyname, int capacity) {
        super(driver, cachingStrategy, keyname, capacity);
    }

    public AsyncQueryCache(CacheDriver<Driver> driver, EvictionPolicy<Integer, Driver> evictionPolicy, String keyname, int capacity) {
        super(driver, evictionPolicy, keyname, capacity);
    }

    @Override
    public CompletionStage<ElementSet<?>> get(Integer key) {
        return super.get(key).thenApply(ElementSet::fromElements); /* TODO: FIND A BETTER WAY */
    }
}
