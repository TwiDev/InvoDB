package ch.twidev.invodb.bridge.cache;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface CacheDriver<Driver> extends EvictionProvider<Driver>{
    <K> void put(String path, K key, byte[] value);
    <K> byte[] get(String path, K key);
    <K> void remove(String path, K key);
    <K> CompletionStage<byte[]> putAsync(String path, K key, byte[] value);
    <K> CompletionStage<byte[]> getAsync(String path, K key);
    <K> CompletionStage<Void> removeAsync(String path, K key);
    <K> boolean has(String path, K key);


    void cleanup(String path);

    Driver getConn();

    @SuppressWarnings("unchecked")
    static String serialize(Object key) {
        long l = System.nanoTime();

        try (ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
             ObjectOutputStream out = new java.io.ObjectOutputStream(bos)) {

            out.writeObject(key);
            String s = UUID.nameUUIDFromBytes(bos.toByteArray()).toString();
            byte[] b = bos.toByteArray();
            System.out.println("S2 " + (System.nanoTime() - l) / 1_000_000);

            return s;
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}