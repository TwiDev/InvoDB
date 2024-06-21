package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import com.google.common.reflect.TypeToken;

import java.io.*;

public abstract class StreamCacheProvider<K extends Serializable, V extends Serializable, Driver>
        implements Cache<K, V>, CacheSerializer, CacheDeserializer {

    private final EvictionPolicy<K, Driver> evictionPolicy;
    private final CacheDriver<Driver> cacheDriver;
    private final Driver driver;
    private final String keyname;
    private final int capacity;
    private final Class<V> valueClass;

    public StreamCacheProvider(CacheDriver<Driver> driver, CachingStrategy cachingStrategy, String keyname, int capacity) {
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
    }

    @Override
    public void put(K key, V value) {
        cacheDriver.put(keyname,
                this.serialize(key),
                this.serialize(value));

        evictionPolicy.onPut(key);
        evictionPolicy.evictIfNecessary();
    }

    @Override
    public V get(K key) {
        V value = this.deserialize(
                cacheDriver.get(keyname, this.serialize(key)), valueClass);

        if (value != null) {
            evictionPolicy.onGet( key);
            return value;
        }

        return null;
    }

    @Override
    public void remove(K key) {
        evictionPolicy.onRemove(key);
    }

    @Override
    public boolean has(K key) {
        return cacheDriver.has(keyname, serialize(key));
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
        try (ByteArrayInputStream bis = new ByteArrayInputStream(value);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            return clazz.cast(ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> byte[] serialize(T value) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(value);

            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
