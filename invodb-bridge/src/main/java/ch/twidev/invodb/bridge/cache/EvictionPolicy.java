package ch.twidev.invodb.bridge.cache;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface EvictionPolicy<K, Driver> {

    void initEviction(boolean isMap);

    void onPut(K key);
    void onGet(K key);
    void onRemove(K key);
    void evictIfNecessary();

    CompletionStage<Boolean> onPutAsync(K key);
    CompletionStage<Boolean> onGetAsync(K key);
    CompletionStage<Boolean> onRemoveAsync(K key);
    CompletionStage<Boolean> evictIfNecessaryAsync();

    String getCacheKey();

    String serialize(K key);
}