package ch.twidev.invodb.bridge.documents;

public interface Elements {

    <T> T getObject(String name, Class<T> type);

    @Deprecated
    <T> T getObject(int id, Class<T> type);

}
