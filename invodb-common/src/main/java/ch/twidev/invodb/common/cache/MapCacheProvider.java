package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.*;

import com.google.j2objc.annotations.OnDealloc;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.swing.plaf.BorderUIResource;

public class MapCacheProvider<Driver> implements MapCache<String> {

    private static final String SEPARATOR = ":";

    private final @Nullable EvictionPolicy<String, Driver> evictionPolicy;

    private final CacheDriver<Driver> cacheDriver;
    private final Driver driver;
    private final String keyname;

    public MapCacheProvider(CacheDriver<Driver> driver, String keyname) {
        this(
                driver,
                null,
                keyname
        );
    }

    public MapCacheProvider(CacheDriver<Driver> driver, @Nullable EvictionPolicy<String, Driver> evictionPolicy, String keyname) {
        this.evictionPolicy = evictionPolicy;
        this.cacheDriver = driver;

        this.driver = cacheDriver.getConn();

        if (this.evictionPolicy != null) {
            this.keyname = evictionPolicy.getCacheKey() + SEPARATOR + keyname;

            this.evictionPolicy.initEviction(true);
        }else{
            this.keyname = keyname;
        }
    }

    @Override
    public void put(String key, Object value) {
        boolean existed = cacheDriver.isMapExists(keyname);

        cacheDriver.getMap(keyname).put(key, value);

        if(evictionPolicy != null && !existed) {
            evictionPolicy.onPut(keyname);
            evictionPolicy.evictIfNecessary();
        }
    }

    @Override
    public <V> V get(String key, Class<V> v) {
        Object o = cacheDriver.getMap(keyname).get(key);

        if(o != null && evictionPolicy != null) {
            evictionPolicy.onGet(keyname);
        }

        return v.cast(o);
    }

    @Override
    public void delete() {
        cacheDriver.removeMap(keyname);

        if(evictionPolicy != null) {
            evictionPolicy.onRemove(keyname);
        }
    }

    @Override
    public boolean has(String key) {
        return cacheDriver.getMap(keyname).containsKey(key);
    }

    @Override
    public String getKeyname() {
        return keyname;
    }

    public String formatKey(String key) {
        return keyname + SEPARATOR + key;
    }
}
