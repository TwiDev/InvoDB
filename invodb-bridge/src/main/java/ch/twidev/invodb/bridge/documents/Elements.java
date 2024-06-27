package ch.twidev.invodb.bridge.documents;

import java.io.Serializable;

public interface Elements extends Serializable {

    boolean isValid();

    Object getObject(String name);

    <T> T getObject(String name, Class<T> type);

    @Deprecated
    <T> T getObject(int id, Class<T> type);

}
