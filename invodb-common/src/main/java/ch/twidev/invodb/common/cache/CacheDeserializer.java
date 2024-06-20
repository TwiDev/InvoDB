package ch.twidev.invodb.common.cache;

public interface CacheDeserializer<V> {

    V deserialize(byte[] value);

}
