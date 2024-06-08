package ch.twidev.invodb.bridge.documents;

public interface Elements {

    boolean isValid();

    Object getObject(String name);

    <T> T getObject(String name, Class<T> type);

    @Deprecated
    <T> T getObject(int id, Class<T> type);

}
