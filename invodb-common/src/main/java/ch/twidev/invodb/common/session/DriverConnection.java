package ch.twidev.invodb.common.session;

import ch.twidev.invodb.common.query.InvoQuery;
import ch.twidev.invodb.common.util.ThrowableCallback;

public abstract class DriverConnection<Pool> {
    public abstract <R> void runQuery(Class<R> resultInstance,
                                      InvoQuery<R> invoQuery,
                                      ThrowableCallback<R> throwableCallback);

    public abstract <R> void runQueryAsync(Class<R> resultInstance,
                                      InvoQuery<R> invoQuery,
                                      ThrowableCallback<R> throwableCallback);

}
