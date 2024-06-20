package ch.twidev.invodb.bridge.cache;

public interface EvictionProvider<Driver> {

    <K> EvictionPolicy<K, Driver> getEvictionPolicy(CachingStrategy cachingStrategy);

}
