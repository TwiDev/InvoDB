package ch.twidev.invodb.bridge.cache;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public interface EvictionPolicy<K, Driver> {
    void onPut(K key);
    void onGet(K key);
    void onRemove(K key);
    void evictIfNecessary();
}