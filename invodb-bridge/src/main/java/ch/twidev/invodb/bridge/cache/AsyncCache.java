package ch.twidev.invodb.bridge.cache;

import java.util.concurrent.CompletionStage;

public interface AsyncCache<K,V> {
    CompletionStage<Void> put(K key, V value);
    CompletionStage<V> get(K key);
    CompletionStage<Void> remove(K key);

    boolean has(K key);

    String getKeyname();

}
