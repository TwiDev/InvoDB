package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import ch.twidev.invodb.common.util.Monitoring;
import com.google.common.reflect.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;

public abstract class StreamCacheProvider<K extends Serializable, V extends Serializable, Driver>
        implements Cache<K, V>, CacheSerializer, CacheDeserializer {

    private final EvictionPolicy<K, Driver> evictionPolicy;
    private final CacheDriver<Driver> cacheDriver;
    private final Driver driver;
    private final String keyname;
    private final int capacity;
    private final Class<V> valueClass;

    public StreamCacheProvider(CacheDriver<Driver> driver, @NonNull CachingStrategy cachingStrategy, String keyname, int capacity) {
        this(
                driver,
                driver.getEvictionPolicy(cachingStrategy, keyname, capacity),
                keyname,
                capacity
        );
    }

    @SuppressWarnings("unchecked")
    public StreamCacheProvider(CacheDriver<Driver> driver, EvictionPolicy<K, Driver> evictionPolicy, String keyname, int capacity) {
        this.evictionPolicy = evictionPolicy;
        this.cacheDriver = driver;
        this.keyname = keyname;
        this.capacity = capacity;

        this.driver = cacheDriver.getConn();

        this.valueClass = (Class<V>) new TypeToken<V>(this.getClass()){}.getRawType();

        this.evictionPolicy.initEviction(false);
    }

    @Override
    public void put(K key, V value) {
        cacheDriver.put(keyname, key,
                this.serialize(value));

        evictionPolicy.onPut(key);
        evictionPolicy.evictIfNecessary();
    }

    @Override
    public V get(K key) {
        Monitoring monitoring = new Monitoring("#2");
        V value = this.deserialize(
                cacheDriver.get(keyname, key), valueClass);

        if (value != null) {
            evictionPolicy.onGet(key);
            monitoring.done();
            return value;
        }

        return null;
    }

    @Override
    public void remove(K key) {
        cacheDriver.remove(keyname, key);

        evictionPolicy.onRemove(key);
    }

    @Override
    public boolean has(K key) {
        Monitoring monitoring = new Monitoring("#1");
        boolean b = cacheDriver.has(keyname, key);
        monitoring.done();
        return b;
    }

    @Override
    public void clear() {
        evictionPolicy.clear();
    }

    @Override
    public String getKeyname() {
        return keyname;
    }

    public Driver getDriver() {
        return driver;
    }

    @Override
    public <T> T deserialize(byte[] value, Class<T> clazz) {
        Monitoring monitoring = new Monitoring("#3 - Mid");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(value);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            T t = clazz.cast(ois.readObject());
            monitoring.done();
            return t;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> byte[] serialize(T value) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeUnshared(value);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
