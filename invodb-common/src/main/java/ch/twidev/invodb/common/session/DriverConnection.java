package ch.twidev.invodb.common.session;

import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ThrowableCallback;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.result.OperationResult;

@SuppressWarnings("unchecked")
public abstract class DriverConnection<Session> {

    protected final DriverSession<Session> session;

    public DriverConnection(DriverSession<Session> session) {
        this.session = session;
    }

    public DriverSession<Session> getSession() {
        return session;
    }

    public <R extends OperationResult> void runQuery(Class<R> resultInstance,
                                                     InvoQuery<R> invoQuery,
                                                     ThrowableCallback<R> throwableCallback) {

        switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> {
                session.find(findOperationBuilder, null);
            }
            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        }

    }

    public <R extends OperationResult> void runQueryAsync(Class<R> resultInstance,
                                  InvoQuery<R> invoQuery,
                                  ThrowableCallback<R> throwableCallback) {

    }

}
