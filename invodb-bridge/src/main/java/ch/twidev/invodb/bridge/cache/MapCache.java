package ch.twidev.invodb.bridge.cache;

public interface MapCache {

    Cache<String, Object> getMap(String key);

    void removeMap(String key);

    boolean isMapExists(String key);

    String getKeyname();

}
