package ch.twidev.invodb.common.cache;

public interface CacheSerializer {

    <T> byte[] serialize(T value);

}
