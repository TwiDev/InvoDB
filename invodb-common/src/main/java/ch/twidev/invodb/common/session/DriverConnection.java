package ch.twidev.invodb.common.session;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ThrowableCallback;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;

@SuppressWarnings("unchecked")
public abstract class DriverConnection<Session> {

    protected final DriverSession<Session> session;

    public DriverConnection(DriverSession<Session> session) {
        this.session = session;
    }

    public DriverSession<Session> getSession() {
        return session;
    }

    public <R> void runQuery(Class<R> resultInstance,
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
