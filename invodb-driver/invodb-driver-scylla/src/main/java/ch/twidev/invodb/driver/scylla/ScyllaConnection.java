package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.contexts.FieldMap;
import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.DeleteContext;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.operations.InsertContext;
import ch.twidev.invodb.bridge.operations.UpdateContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.search.ISearchFilter;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ResultCallback;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScyllaConnection implements DriverSession<Session> {

    private static final ExecutorService QUERY_EXECUTOR = Executors.newCachedThreadPool();

    public static SearchDictionary searchDictionary = new SearchDictionary(){{
        put(SearchFilterType.ALL, new SearchCompositeParameter("*"));
        put(SearchFilterType.AND, new SearchCompositeParameter("AND"));
        put(SearchFilterType.OR, new SearchCompositeParameter("OR"));
        put(SearchFilterType.EQUAL, new SearchFieldParameter() {
            @Override
            public String parse(String key, List<Object> context) {
                return key + " = ?";
            }
        });
        put(SearchFilterType.NOT_EQUAL, new SearchFieldParameter() {
            @Override
            public String parse(String key, List<Object> context) {
                return key + " != ?";
            }
        });
        put(SearchFilterType.IN, new SearchFieldParameter() {
            @Override
            public String parse(String key, List<Object> context) {
                String sub = ("?,".repeat(context.size()));
                return key + " in(" + sub.substring(0, sub.length() - 1) + ")";
            }
        });
    }};

    private final Cache<?,?> queryCache;
    private final Session session;

    public ScyllaConnection(Session session, Cache<?,?> queryCache) {
        this.session = session;
        this.queryCache = queryCache;
    }

    @Override
    public Cache<?, ?> getQueryCache() {
        return queryCache;
    }

    @Override
    public boolean isConnected() {
        return session != null && !session.isClosed();
    }

    @Override
    public Session getLegacySession() {
        return session;
    }

    @Override
    public ElementSet<?> find(FindContext findOperationBuilder, PlaceholderContext placeholderContext) {
        ISearchFilter searchFilter = findOperationBuilder.getSearchFilter();

        try {
            String statement = "SELECT %s FROM %s ".formatted(findOperationBuilder.getAttributes().toString(), findOperationBuilder.getCollection())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) : "")
                    + (findOperationBuilder.hasLimit() ? " LIMIT " + findOperationBuilder.getLimit() : "");

            System.out.println(statement);

            ResultSet resultSet = searchFilter.isRequired() ?
                    session.execute(statement, searchFilter.getContexts().toArray(new Object[0])) :
                    session.execute(statement);

            return new ScyllaResultSet(resultSet);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public CompletableFuture<ElementSet<?>> findAsync(FindContext findOperationBuilder, PlaceholderContext placeholderContext) {
        ISearchFilter searchFilter = findOperationBuilder.getSearchFilter();
        CompletableFuture<ElementSet<?>> completableFuture = new CompletableFuture<>();

        try {
            String statement = "SELECT %s FROM %s ".formatted(findOperationBuilder.getAttributes().toString(), findOperationBuilder.getCollection())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) : "")
                    + (findOperationBuilder.hasLimit() ? " LIMIT " + findOperationBuilder.getLimit() : "");

            ResultSetFuture resultSet = searchFilter.isRequired() ?
                    session.executeAsync(statement, searchFilter.getContexts().toArray(new Object[0])) :
                    session.executeAsync(statement);

            Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
                @Override
                public void onSuccess(ResultSet result) {
                    completableFuture.complete(new ScyllaResultSet(result));
                }

                @Override
                public void onFailure(@NotNull Throwable exception) {
                    completableFuture.completeExceptionally(exception);
                }
            }, QUERY_EXECUTOR);

            return completableFuture;
        } catch (Exception exception) {
            completableFuture.completeExceptionally(exception);
        }

        return completableFuture;
    }

    @Override
    public OperationResult update(UpdateContext updateContext, PlaceholderContext placeholderContext) {
        ISearchFilter searchFilter = updateContext.getSearchFilter();

        try {
            FieldMap fields = updateContext.getFields().getFormattedFields(placeholderContext);

            String statement = "UPDATE %s SET %s ".formatted(updateContext.getCollection(), fields.toString())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) + " IF EXISTS" : "");

            System.out.println(statement);

            List<Object> context = new ArrayList<>(fields.values());
            context.addAll(updateContext.getContexts());

            System.out.println(context);

            ResultSet resultSet = searchFilter.isRequired() ?
                    session.execute(statement, context.toArray(new Object[0])) :
                    session.execute(statement);

            return OperationResult.Ok;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public CompletableFuture<OperationResult> updateAsync(UpdateContext updateContext, PlaceholderContext placeholderContext) {
        CompletableFuture<OperationResult> completableFuture = new CompletableFuture<>();

        try {
            ISearchFilter searchFilter = updateContext.getSearchFilter();
            FieldMap fields = updateContext.getFields().getFormattedFields(placeholderContext);

            String statement = "UPDATE %s SET %s ".formatted(updateContext.getCollection(), fields.toString())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) : "");

            List<Object> context = new ArrayList<>(fields.values());
            context.addAll(updateContext.getContexts());

            ResultSetFuture resultSet = searchFilter.isRequired() ?
                    session.executeAsync(statement, context.toArray(new Object[0])) :
                    session.executeAsync(statement);

            Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
                @Override
                public void onSuccess(ResultSet result) {
                    completableFuture.complete(OperationResult.Ok);
                }

                @Override
                public void onFailure(@NotNull Throwable exception) {
                    completableFuture.completeExceptionally(exception);
                }
            }, QUERY_EXECUTOR);
        } catch (Exception exception) {
            completableFuture.completeExceptionally(exception);
        }

        return completableFuture;
    }

    @Override
    public OperationResult insert(InsertContext updateContext, PlaceholderContext placeholderContext) {
        try {
            FieldMap fields = updateContext.getFields().getFormattedFields(placeholderContext);

            String statement = "INSERT INTO %s (%s) VALUES (%s) IF NOT EXISTS".formatted(
                    updateContext.getCollection(),
                    fields.getKeysString(),
                    fields.getUnbounded());

            ResultSet resultSet = session.execute(statement,
                    fields.values().toArray(new Object[0]));

            if(!resultSet.wasApplied()) {
                return OperationResult.Err;
            }

            return OperationResult.Ok;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public CompletableFuture<OperationResult> insertAsync(InsertContext updateContext, PlaceholderContext placeholderContext) {
        CompletableFuture<OperationResult> completableFuture = new CompletableFuture<>();

        try {
            FieldMap fields = updateContext.getFields().getFormattedFields(placeholderContext);

            String statement = "INSERT INTO %s (%s) VALUES (%s) IF NOT EXISTS".formatted(
                    updateContext.getCollection(),
                    fields.getKeysString(),
                    fields.getUnbounded());

            ResultSetFuture resultSet = session.executeAsync(statement,
                    fields.values().toArray(new Object[0]));

            Futures.addCallback(resultSet, new FutureCallback<>() {
                @Override
                public void onSuccess(ResultSet rows) {
                    if(!rows.wasApplied()) {
                        completableFuture.complete(
                                OperationResult.Err
                        );
                    }

                    completableFuture.complete(
                            OperationResult.Ok
                    );
                }

                @Override
                public void onFailure(Throwable throwable) {
                    completableFuture.completeExceptionally(throwable);
                }
            }, QUERY_EXECUTOR);
        } catch (Exception e) {
            completableFuture.completeExceptionally(e);
        }

        return completableFuture;
    }

    @Override
    public OperationResult delete(DeleteContext updateContext, PlaceholderContext placeholderContext) {
        try {
            ISearchFilter searchFilter = updateContext.getSearchFilter();

            String statement = "DELETE FROM %s ".formatted(updateContext.getCollection())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) : "");

            ResultSet resultSet = searchFilter.isRequired() ?
                    session.execute(statement, updateContext.getContexts().toArray(new Object[0])) :
                    session.execute(statement);

            return OperationResult.Ok;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<OperationResult> deleteAsync(DeleteContext updateContext, PlaceholderContext placeholderContext) {
        CompletableFuture<OperationResult> completableFuture = new CompletableFuture<>();

        try {
            ISearchFilter searchFilter = updateContext.getSearchFilter();

            String statement = "DELETE FROM %s ".formatted(updateContext.getCollection())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) : "");

            ResultSetFuture resultSet = searchFilter.isRequired() ?
                    session.executeAsync(statement, updateContext.getContexts().toArray(new Object[0])) :
                    session.executeAsync(statement);

            Futures.addCallback(resultSet, new FutureCallback<>() {
                @Override
                public void onSuccess(ResultSet rows) {
                    completableFuture.complete(
                            OperationResult.Ok
                    );
                }

                @Override
                public void onFailure(Throwable throwable) {
                    completableFuture.completeExceptionally(throwable);
                }
            }, QUERY_EXECUTOR);

            return completableFuture;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        session.close();
    }
}
