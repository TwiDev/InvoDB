package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.bridge.documents.ElementSet;

import java.io.Serializable;

public class QueryCache<Driver> extends StreamCacheProvider<Integer, ElementSet<?>, Driver> {

    public QueryCache(CacheDriver<Driver> driver, CachingStrategy cachingStrategy, String keyname, int capacity) {
        super(driver, cachingStrategy, keyname, capacity);
    }

    public QueryCache(CacheDriver<Driver> driver, EvictionPolicy<Integer, Driver> evictionPolicy, String keyname, int capacity) {
        super(driver, evictionPolicy, keyname, capacity);
    }

    @Override
    public ElementSet<?> get(Integer key) {
        return super.get(key).fromElements(); /* TODO: FIND A BETTER WAY */
    }
}
