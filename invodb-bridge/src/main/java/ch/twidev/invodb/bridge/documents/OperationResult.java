package ch.twidev.invodb.bridge.documents;

public interface OperationResult {

    OperationResult Ok = () -> 0;

    OperationResult Err = () -> 0;

    long getTime();

    default boolean isCached() {
        return false;
    }

    default boolean isOk() {
        return this == Ok;
    }

}
