package ch.twidev.invodb.common.cache;

public interface CacheDeserializer {

    <T> T deserialize(byte[] value, Class<T> clazz);

}
