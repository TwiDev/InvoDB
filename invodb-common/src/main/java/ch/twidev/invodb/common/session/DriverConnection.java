package ch.twidev.invodb.common.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ThrowableCallback;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;

@SuppressWarnings("unchecked")
public class DriverConnection {

    public static <Session, R> void runQuery(DriverSession<Session> session,
                             Class<R> resultInstance,
                             InvoQuery<R> invoQuery,
                             ThrowableCallback<R> throwableCallback) {

        switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> {
                session.find(findOperationBuilder, (ThrowableCallback<ElementSet>) throwableCallback);
            }
            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        }

    }

    public <R> void runQueryAsync(Class<R> resultInstance,
                                  InvoQuery<R> invoQuery,
                                  ThrowableCallback<R> throwableCallback) {

    }

}
