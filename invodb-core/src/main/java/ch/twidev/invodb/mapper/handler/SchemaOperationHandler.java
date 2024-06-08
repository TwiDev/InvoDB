package ch.twidev.invodb.mapper.handler;

public interface SchemaOperationHandler<T> {

    void onFailed(Throwable e);

}
