package ch.twidev.invodb.common.session;

import ch.twidev.invodb.bridge.session.ISession;
import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.result.OperationResult;
import ch.twidev.invodb.common.result.ResultSet;
import ch.twidev.invodb.bridge.util.ThrowableCallback;

@SuppressWarnings("unchecked")
public abstract class DriverConnection<Session> {

    protected final ISession<Session> session;

    public DriverConnection(ISession<Session> session) {
        this.session = session;
    }

    public ISession<Session> getSession() {
        return session;
    }

    public <R extends OperationResult> void runQuery(Class<R> resultInstance,
                                                     InvoQuery<R> invoQuery,
                                                     ThrowableCallback<R> throwableCallback) {

        switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> {
                session.find(findOperationBuilder, (ThrowableCallback<ResultSet>) throwableCallback);
            }
            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        }

    }

    public <R extends OperationResult> void runQueryAsync(Class<R> resultInstance,
                                  InvoQuery<R> invoQuery,
                                  ThrowableCallback<R> throwableCallback) {

    }

}
