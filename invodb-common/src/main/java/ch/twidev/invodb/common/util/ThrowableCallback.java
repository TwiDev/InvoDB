package ch.twidev.invodb.common.util;

public interface ThrowableCallback<T> {

    void run(T t, Throwable throwable);

}
