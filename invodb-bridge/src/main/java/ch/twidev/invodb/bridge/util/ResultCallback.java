package ch.twidev.invodb.bridge.util;

public interface ResultCallback<T>{

    void succeed(T t);

    default void failed(Throwable throwable) {};

}
