package ch.twidev.invodb.common.query;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.operations.OperationContext;
import ch.twidev.invodb.bridge.operations.SearchContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.common.query.builder.DeleteOperationBuilder;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.builder.InsertOperationBuilder;
import ch.twidev.invodb.common.query.builder.UpdateOperationBuilder;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import ch.twidev.invodb.common.session.DriverConnection;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class InvoQuery<Result> implements OperationContext{

    public static FindOperationBuilder find(String collection) {
        return new FindOperationBuilder(collection);
    }

    public static UpdateOperationBuilder update(String collection) {
        return new UpdateOperationBuilder(collection);
    }

    public static InsertOperationBuilder insert(String collection) {
        return new InsertOperationBuilder(collection);
    }

    public static DeleteOperationBuilder delete(String collection) {
        return new DeleteOperationBuilder(collection);
    }

    private final QueryOperation queryOperation;
    private final String collection;

    public InvoQuery(String collection, QueryOperation queryOperation) {
        this.collection = collection;
        this.queryOperation = queryOperation;
    }

    public Result run(@NotNull DriverSession<?> driverConnection) {
        return this.run(driverConnection, null);
    }

    public CompletableFuture<Result> runAsync(@NotNull DriverSession<?> driverConnection) {
        return this.runAsync(driverConnection, null);
    }

    public Result run(@NotNull DriverSession<?> driverConnection, PlaceholderContext placeholderContext) {
        return this.execute(driverConnection, placeholderContext);
    }

    public CompletableFuture<Result> runAsync(DriverSession<?> driverConnection, PlaceholderContext placeholderContext) {
        return DriverConnection.runQueryAsync(driverConnection, this, placeholderContext);
    }

    protected abstract Result execute(DriverSession<?> driverSession, PlaceholderContext placeholderContext);
    protected abstract CompletableFuture<Result> executeAsync(DriverSession<?> driverSession, PlaceholderContext placeholderContext);

    public QueryOperation getQueryOperation() {
        return queryOperation;
    }

    public String getCollection() {
        return collection;
    }

    @SuppressWarnings("unchecked")
    public static <Session> QueryCache<Session> getQueryCache(DriverSession<Session> session) {
        Cache<?,?> cache = session.getQueryCache();
        if(cache == null) return null;
        if(cache instanceof QueryCache<?> queryCache) {
            return (QueryCache<Session>) queryCache;
        }

        return null;
    }
}
