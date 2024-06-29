package ch.twidev.invodb.bridge.cache;

public interface EvictionProvider<Driver> {

    <K> EvictionPolicy<K, Driver> getEvictionPolicy(CachingStrategy cachingStrategy, String keyname, int capacity, boolean isMap);
    default <K> EvictionPolicy<K, Driver> getEvictionPolicy(CachingStrategy cachingStrategy, String keyname, int capacity) {
        return this.getEvictionPolicy(cachingStrategy, keyname, capacity, false);
    }

}
