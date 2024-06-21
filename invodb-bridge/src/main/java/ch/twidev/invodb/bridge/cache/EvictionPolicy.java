package ch.twidev.invodb.bridge.cache;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public interface EvictionPolicy<K, Driver> {
    void onPut(K key);
    void onGet(K key);
    void onRemove(K key);
    void evictIfNecessary();

    @SuppressWarnings("unchecked")
    default String serialize(K key) {
        try (ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
             ObjectOutputStream out = new java.io.ObjectOutputStream(bos)) {

            out.writeObject(key);
            return UUID.nameUUIDFromBytes(bos.toByteArray()).toString();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}