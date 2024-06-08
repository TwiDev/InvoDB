package ch.twidev.invodb.mapper.handler;

import ch.twidev.invodb.common.query.InvoQuery;

public interface SchemaOperationHandler {

    SchemaOperationHandler HANDLER = new SchemaOperationHandler(){};

    default void onSuccess(InvoQuery<?> invoQuery) {};

    default void onFailed(Throwable e) {};

}
