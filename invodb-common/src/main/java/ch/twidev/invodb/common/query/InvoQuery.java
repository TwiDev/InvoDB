package ch.twidev.invodb.common.query;

import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.result.OperationResult;
import ch.twidev.invodb.common.session.DriverConnection;
import ch.twidev.invodb.bridge.util.ThrowableCallback;

public abstract class InvoQuery<Result extends OperationResult> {

    public static FindOperationBuilder find(String collection) {
        return new FindOperationBuilder(collection);
    }

    private final QueryOperation queryOperation;
    private final String collection;
    private final Class<Result> resultInstance;

    public InvoQuery(Class<Result> resultInstance, String collection, QueryOperation queryOperation) {
        this.collection = collection;
        this.queryOperation = queryOperation;
        this.resultInstance = resultInstance;
    }

    public void run(DriverConnection<?> driverConnection, ThrowableCallback<Result> resultSet) {
        driverConnection.runQuery(resultInstance, this, resultSet);
    }

    public void runAsync(DriverConnection<?> driverConnection, ThrowableCallback<Result> resultSet) {
        driverConnection.runQueryAsync(resultInstance, this, resultSet);
    }

    public QueryOperation getQueryOperation() {
        return queryOperation;
    }

    public String getCollection() {
        return collection;
    }
}
