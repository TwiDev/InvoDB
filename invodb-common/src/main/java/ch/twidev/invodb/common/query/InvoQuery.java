package ch.twidev.invodb.common.query;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.operations.OperationContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.common.cache.QueryCache;
import ch.twidev.invodb.common.query.builder.DeleteOperationBuilder;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.builder.InsertOperationBuilder;
import ch.twidev.invodb.common.query.builder.UpdateOperationBuilder;
import ch.twidev.invodb.common.query.operations.QueryOperation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class InvoQuery<Result> implements OperationContext{

    /**
     * Creates a new {@link FindOperationBuilder} for the specified collection.
     *
     * <p> This query is used to find documents within a collection that match specified criteria.
     *
     * @param collection the collection name
     * @return a new FindOperationBuilder instance
     */
    public static FindOperationBuilder find(String collection) {
        return new FindOperationBuilder(collection);
    }

    /**
     * Creates a new {@link UpdateOperationBuilder} for the specified collection.
     *
     * <p>This query is used to update documents within a collection that match specified criteria.
     *
     * @param collection the collection name
     * @return a new UpdateOperationBuilder instance
     */
    public static UpdateOperationBuilder update(String collection) {
        return new UpdateOperationBuilder(collection);
    }

    /**
     * Creates a new {@link InsertOperationBuilder} for the specified collection.
     *
     * <p>This query is used to insert new documents into a collection.
     *
     * @param collection the collection name
     * @return a new InsertOperationBuilder instance
     */
    public static InsertOperationBuilder insert(String collection) {
        return new InsertOperationBuilder(collection);
    }

    /**
     * Creates a new {@link DeleteOperationBuilder} for the specified collection.
     *
     * <p>This query is used to delete documents from a collection that match specified criteria.
     *
     * @param collection the collection name
     * @return a new DeleteOperationBuilder instance
     */
    public static DeleteOperationBuilder delete(String collection) {
        return new DeleteOperationBuilder(collection);
    }

    private final QueryOperation queryOperation;
    private final String collection;

    protected PlaceholderContext placeholderContext = new PlaceholderContext();

    public InvoQuery(String collection, QueryOperation queryOperation) {
        this.collection = collection;
        this.queryOperation = queryOperation;
    }

    /**
     * Executes the query synchronously using the specified driver connection without any context.
     *
     * @param driverConnection the driver connection
     * @return the result of the query
     */
    public Result run(@NotNull DriverSession<?> driverConnection) {
        return this.run(driverConnection, null);
    }

    /**
     * Executes the query asynchronously using the specified driver connection without any context.
     *
     * @param driverConnection the driver connection
     * @return a CompletableFuture representing the result of the query
     */
    public CompletableFuture<Result> runAsync(@NotNull DriverSession<?> driverConnection) {
        return this.runAsync(driverConnection, null);
    }

    /**
     * Executes the query synchronously using the specified driver connection and placeholder context.
     *
     * @param driverConnection   the driver connection
     * @param placeholderContext the placeholder context
     * @return the result of the query
     */
    public Result run(@NotNull DriverSession<?> driverConnection, PlaceholderContext placeholderContext) {
        Result result = this.execute(driverConnection, placeholderContext);

        return this.handleResult(driverConnection, result);
    }

    /**
     * Executes the query asynchronously using the specified driver connection and placeholder context.
     *
     * @param driverConnection   the driver connection
     * @param placeholderContext the placeholder context
     * @return a CompletableFuture representing the result of the query
     */
    public CompletableFuture<Result> runAsync(DriverSession<?> driverConnection, PlaceholderContext placeholderContext) {
        CompletableFuture<Result> result = this.executeAsync(driverConnection, placeholderContext);

        return result.thenApply(handler -> handleResult(driverConnection, handler));
    }

    /**
     * Executes the query without any handling result.
     *
     * @param driverSession      the driver session
     * @param placeholderContext the placeholder context
     * @return the result of the query
     */
    protected abstract Result execute(DriverSession<?> driverSession, PlaceholderContext placeholderContext);

    /**
     * Executes the query without any handling result.
     *
     * @param driverSession      the driver session
     * @param placeholderContext the placeholder context
     * @return a CompletableFuture representing the result of the query
     */
    protected abstract CompletableFuture<Result> executeAsync(DriverSession<?> driverSession, PlaceholderContext placeholderContext);

    /**
     * Handles the result of the query.
     *
     * @param result the result to handle
     * @return the handled result
     */
    public Result handleResult(DriverSession<?> driverSession, Result result) {
        return result;
    }

    public PlaceholderContext getPlaceholderContext() {
        return placeholderContext;
    }

    public QueryOperation getQueryOperation() {
        return queryOperation;
    }

    public String getCollection() {
        return collection;
    }

    /**
     * Gets the query cache for the specified session.
     *
     * @param <Session> the session type
     * @param session   the driver session
     * @return the query cache, or null if none is available
     */
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
