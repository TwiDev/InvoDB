package ch.twidev.invodb.future;

public interface FutureThrowableCallback<T> {

    void call(T value, Throwable throwable);

}
