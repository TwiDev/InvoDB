package ch.twidev.invodb.bridge.util;

public interface ThrowableCallback<T>{

    void run(T t, Throwable throwable);

}
