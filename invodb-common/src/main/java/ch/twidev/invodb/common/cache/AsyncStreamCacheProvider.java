package ch.twidev.invodb.common.cache;

import ch.twidev.invodb.bridge.cache.AsyncCache;
import ch.twidev.invodb.bridge.cache.CacheDriver;
import ch.twidev.invodb.bridge.cache.CachingStrategy;
import ch.twidev.invodb.bridge.cache.EvictionPolicy;
import com.google.common.reflect.TypeToken;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public abstract class AsyncStreamCacheProvider<K extends Serializable, V extends Serializable, Driver>
        implements AsyncCache<K, V>, CacheSerializer, CacheDeserializer{

    private final EvictionPolicy<K, Driver> evictionPolicy;
    private final CacheDriver<Driver> cacheDriver;
    private final Driver driver;
    private final String keyname;
    private final int capacity;
    private final Class<V> valueClass;

    @SuppressWarnings("unchecked")
    public AsyncStreamCacheProvider(CacheDriver<Driver> driver, EvictionPolicy<K, Driver> evictionPolicy, String keyname, int capacity) {
        this.evictionPolicy = evictionPolicy;
        this.cacheDriver = driver;
        this.keyname = keyname;
        this.capacity = capacity;

        this.driver = cacheDriver.getConn();

        this.valueClass = (Class<V>) new TypeToken<V>(this.getClass()){}.getRawType();
    }

    @Override
    public CompletionStage<Void> put(K key, V value) {
        return cacheDriver.putAsync(keyname, key, this.serialize(value))
                .thenCompose(bytes -> evictionPolicy.onPutAsync(key))
                .thenCompose(bytes -> evictionPolicy.evictIfNecessaryAsync())
                .thenApply(bool -> null);
    }

    @Override
    public CompletionStage<V> get(K key) {
        return cacheDriver.getAsync(keyname, key)
                .thenCompose(bytes -> {
                    V value = this.deserialize(bytes, valueClass);
                    if (value != null) {
                        return evictionPolicy.onGetAsync(key).thenApply(aVoid -> value);
                    } else {
                        return CompletableFuture.completedFuture(null);
                    }
                });
    }

    @Override
    public CompletionStage<Void> remove(K key) {
        return cacheDriver.removeAsync(keyname, key)
                .thenCompose(bytes -> evictionPolicy.onRemoveAsync(key))
                .thenRun(() -> {});
    }

    @Override
    public boolean has(K key) {
        return cacheDriver.has(keyname, key);
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
            byte[] b = bos.toByteArray();
            return b;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
