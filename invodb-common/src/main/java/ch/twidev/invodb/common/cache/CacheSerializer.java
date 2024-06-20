package ch.twidev.invodb.common.cache;

public interface CacheSerializer<V> {

    byte[] serialize(V value);

}
