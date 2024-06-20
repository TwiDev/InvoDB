package ch.twidev.invodb.common.cache.lazy;

import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.common.cache.StreamCacheProvider;

public class LazyLoadStreamCacheProvider<K,V,Driver extends CacheDriver<Driver>> extends StreamCacheProvider<K, V, Driver> {

    private final DataLoader<K, V> dataLoader;

    public LazyLoadStreamCacheProvider(Driver driver, CachingStrategy cachingStrategy, String keyname, int capacity, DataLoader<K, V> dataLoader) {
        super(driver, cachingStrategy, keyname, capacity);

        this.dataLoader = dataLoader;
    }

    public LazyLoadStreamCacheProvider(Driver driver, EvictionPolicy<K, Driver> evictionPolicy, String keyname, int capacity, DataLoader<K, V> dataLoader) {
        super(driver, evictionPolicy, keyname, capacity);

        this.dataLoader = dataLoader;
    }

    @Override
    public V get(K key) {
        V value = super.get(key);
        if (value == null) {
            value = dataLoader.load(key);
            if (value != null) {
                super.put(key, value);
            }
        }
        return value;
    }

    public DataLoader<K, V> getDataLoader() {
        return dataLoader;
    }
}
