package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.bridge.documents.ElementSetWrapper;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.common.documents.CachedResultSet;

import java.util.concurrent.CompletionStage;

public class AsyncQueryCache<Driver> extends AsyncStreamCacheProvider<Integer, ElementSetWrapper<? extends Elements>, Driver> {
    public AsyncQueryCache(CacheDriver<Driver> driver, CachingStrategy cachingStrategy, String keyname, int capacity) {
        super(driver, null, keyname, capacity);
    }

    public AsyncQueryCache(CacheDriver<Driver> driver, EvictionPolicy<Integer, Driver> evictionPolicy, String keyname, int capacity) {
        super(driver, evictionPolicy, keyname, capacity);
    }

    @Override
    public CompletionStage<ElementSetWrapper<? extends Elements>> get(Integer key) {
        return super.get(key);
    }

    public CompletionStage<CachedResultSet> getSet(Integer key) {
        CompletionStage<ElementSetWrapper<? extends Elements>> wrapper = super.get(key);

        return wrapper.thenApply(elementSetWrapper -> new CachedResultSet(elementSetWrapper.getElements(), elementSetWrapper));
    }
}
