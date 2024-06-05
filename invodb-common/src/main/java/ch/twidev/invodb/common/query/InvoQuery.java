package ch.twidev.invodb.common.query;

import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.session.DriverConnection;
import ch.twidev.invodb.bridge.util.ResultCallback;
import org.jetbrains.annotations.NotNull;

public abstract class InvoQuery<Result> {

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

    public void run(@NotNull DriverSession<?> driverConnection, ResultCallback<Result> resultSet) {
        this.run(driverConnection, resultSet, null);
    }

    public void runAsync(@NotNull DriverSession<?> driverConnection, ResultCallback<Result> resultSet) {
        this.runAsync(driverConnection, resultSet, null);
    }

    public void run(@NotNull DriverSession<?> driverConnection, ResultCallback<Result> resultSet, PlaceholderContext placeholderContext) {
        DriverConnection.runQuery(driverConnection, resultInstance, this, resultSet, placeholderContext);
    }

    public void runAsync(DriverSession<?> driverConnection, ResultCallback<Result> resultSet, PlaceholderContext placeholderContext) {
        DriverConnection.runQueryAsync(driverConnection, resultInstance, this, resultSet, placeholderContext);
    }

    public QueryOperation getQueryOperation() {
        return queryOperation;
    }

    public String getCollection() {
        return collection;
    }
}
