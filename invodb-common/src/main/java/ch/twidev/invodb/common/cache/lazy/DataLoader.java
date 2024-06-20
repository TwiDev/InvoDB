package ch.twidev.invodb.common.cache.lazy;

public interface DataLoader<K, V> {

    V load(K key);
}
