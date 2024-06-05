package ch.twidev.invodb.common.util;

import ch.twidev.invodb.bridge.util.ResultCallback;

import org.jetbrains.annotations.Nullable;

public interface ThrowableCallback<T> extends ResultCallback<T> {

    void run(@Nullable T t, @Nullable Throwable throwable);

    @Override
    default void succeed(T t) {
        this.run(t, null);
    }

    @Override
    default void failed(Throwable throwable) {
        this.run(null, throwable);
    }
}
