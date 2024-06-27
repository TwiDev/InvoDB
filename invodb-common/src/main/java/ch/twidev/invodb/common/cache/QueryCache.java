package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.ElementSetWrapper;
import ch.twidev.invodb.bridge.documents.Elements;
import ch.twidev.invodb.common.documents.ResultSet;

import java.io.Serializable;

public class QueryCache<Driver> extends StreamCacheProvider<Integer, ElementSetWrapper<? extends Elements>, Driver> {

    public QueryCache(CacheDriver<Driver> driver, CachingStrategy cachingStrategy, String keyname, int capacity) {
        super(driver, cachingStrategy, keyname, capacity);
    }

    public QueryCache(CacheDriver<Driver> driver, EvictionPolicy<Integer, Driver> evictionPolicy, String keyname, int capacity) {
        super(driver, evictionPolicy, keyname, capacity);
    }

    @Override
    public ElementSetWrapper<? extends Elements> get(Integer key) {
        return super.get(key);
    }

    public ResultSet getSet(Integer key) {
        ElementSetWrapper<? extends Elements> wrapper = super.get(key);

        return new ResultSet(wrapper.getElements(), wrapper);
    }
}
