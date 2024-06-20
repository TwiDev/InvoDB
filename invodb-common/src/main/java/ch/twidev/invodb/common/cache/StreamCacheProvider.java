package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;

import java.io.*;

public class StreamCacheProvider<K, V, Driver> implements Cache<K, V>, CacheSerializer<V>, CacheDeserializer<V> {

    private final EvictionPolicy<K, Driver> evictionPolicy;
    private final CacheDriver<Driver> cacheDriver;
    private final Driver driver;
    private final String keyname;
    private final int capacity;

    public StreamCacheProvider(CacheDriver<Driver> driver, CachingStrategy cachingStrategy, String keyname, int capacity) {
        this(
                driver,
                driver.getEvictionPolicy(cachingStrategy),
                keyname,
                capacity
        );
    }

    public StreamCacheProvider(CacheDriver<Driver> driver, EvictionPolicy<K, Driver> evictionPolicy, String keyname, int capacity) {
        this.evictionPolicy = evictionPolicy;
        this.cacheDriver = driver;
        this.keyname = keyname;
        this.capacity = capacity;

        this.driver = cacheDriver.getConn();
    }

    @Override
    public void put(K key, V value) {
        cacheDriver.put(key,
                this.serialize(value));

        evictionPolicy.onPut(driver, keyname, key);
        evictionPolicy.evictIfNecessary(driver, keyname, capacity);
    }

    @Override
    public V get(K key) {
        V value = this.deserialize(
                cacheDriver.get(key));

        if (value != null) {
            evictionPolicy.onGet(driver, keyname, key);
            return value;
        }

        return null;
    }

    @Override
    public void remove(K key) {
        evictionPolicy.onRemove(driver, keyname, key);
    }

    @Override
    public String getKeyname() {
        return keyname;
    }

    public Driver getDriver() {
        return driver;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V deserialize(byte[] value) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(value);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            return (V) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serialize(V value) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(value);

            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
