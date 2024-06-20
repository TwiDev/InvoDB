package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;

public class QueryCache<Driver extends CacheDriver<Driver>> extends StreamCacheProvider<Integer, Object, Driver> {
    public QueryCache(Driver driver, CachingStrategy cachingStrategy, String keyname, int capacity) {
        super(driver, cachingStrategy, keyname, capacity);
    }
}
