package ch.twidev.invodb.common.session;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.result.OperationResult;
import ch.twidev.invodb.common.result.ResultSet;
import ch.twidev.invodb.common.util.ThrowableCallback;

@SuppressWarnings("unchecked")
public abstract class DriverConnection<Session> {

    protected final Session session;

    public DriverConnection(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public <R extends OperationResult> void runQuery(Class<R> resultInstance,
                                                     InvoQuery<R> invoQuery,
                                                     ThrowableCallback<R> throwableCallback) {

        switch (invoQuery) {
            case FindOperationBuilder findOperationBuilder -> {
                this.find(findOperationBuilder, (ThrowableCallback<ResultSet>) throwableCallback);
            }
            default -> throw new IllegalStateException("Unexpected value: " + invoQuery);
        }

    }

    public <R extends OperationResult> void runQueryAsync(Class<R> resultInstance,
                                  InvoQuery<R> invoQuery,
                                  ThrowableCallback<R> throwableCallback) {

    }

    public abstract boolean isConnected();

    public abstract void find(FindOperationBuilder findOperationBuilder, ThrowableCallback<ResultSet> throwableCallback);

}
