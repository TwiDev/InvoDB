package ch.twidev.invodb.bridge.cache;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

public interface CacheDriver<Driver> extends EvictionProvider<Driver>{
    <K>  void put(String path, K key, byte[] value);
    <K> byte[] get(String path, K key);
    <K> void remove(String path, K key);

    <K>  boolean has(String path, K key);

    void cleanup(String path);

    Driver getConn();

    @SuppressWarnings("unchecked")
    default String serialize(Object key) {
        try (ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
             ObjectOutputStream out = new java.io.ObjectOutputStream(bos)) {

            out.writeObject(key);
            return UUID.nameUUIDFromBytes(bos.toByteArray()).toString();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}