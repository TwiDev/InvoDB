package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.operations.UpdateContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.search.ISearchFilter;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ResultCallback;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class ScyllaConnection implements DriverSession<Session> {

    private static final ExecutorService QUERY_EXECUTOR = Executors.newCachedThreadPool();

    private static final SearchDictionary searchDictionary = new SearchDictionary(){{
        put(SearchFilterType.ALL, new SearchCompositeParameter("*"));
        put(SearchFilterType.AND, new SearchCompositeParameter("AND"));
        put(SearchFilterType.OR, new SearchCompositeParameter("OR"));
        put(SearchFilterType.EQUAL, new SearchFieldParameter() {
            @Override
            public String parse(String key) {
                return key + " = ?";
            }
        });
        put(SearchFilterType.NOT_EQUAL, new SearchFieldParameter() {
            @Override
            public String parse(String key) {
                return key + " != ?";
            }
        });
    }};

    private final Session session;

    public ScyllaConnection(Session session) {
        this.session = session;
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
    public void find(FindContext findOperationBuilder, PlaceholderContext placeholderContext, ResultCallback<ElementSet> throwableCallback) {
        ISearchFilter searchFilter = findOperationBuilder.getSearchFilter();

        try {
            String statement = "SELECT %s FROM %s ".formatted(findOperationBuilder.getAttributes().toString(), findOperationBuilder.getCollection())
                            + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) + " ALLOW FILTERING" : "");

            ResultSet resultSet = searchFilter.isRequired() ?
                    session.execute(statement, searchFilter.getContexts().toArray(new Object[0])) :
                    session.execute(statement);

            throwableCallback.succeed(new ScyllaResultSet(resultSet));
        } catch (Exception exception) {
            throwableCallback.failed(exception);
        }

    }

    @Override
    public void findAsync(FindContext findOperationBuilder, PlaceholderContext placeholderContext, ResultCallback<ElementSet> throwableCallback) {
        ISearchFilter searchFilter = findOperationBuilder.getSearchFilter();

        try {
            String statement = "SELECT %s FROM %s ".formatted(findOperationBuilder.getAttributes().toString(), findOperationBuilder.getCollection())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) + " ALLOW FILTERING" : "");

            ResultSetFuture resultSet = searchFilter.isRequired() ?
                    session.executeAsync(statement, searchFilter.getContexts().toArray(new Object[0])) :
                    session.executeAsync(statement);

            Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
                @Override
                public void onSuccess(ResultSet result) {
                    throwableCallback.succeed(new ScyllaResultSet(result));
                }

                @Override
                public void onFailure(@NotNull Throwable exception) {
                    throwableCallback.failed(exception);
                }
            }, QUERY_EXECUTOR);
        } catch (Exception exception) {
            throwableCallback.failed(exception);
        }
    }

    @Override
    public void update(UpdateContext updateContext, PlaceholderContext placeholderContext, ResultCallback<OperationResult> callback) {
        ISearchFilter searchFilter = updateContext.getSearchFilter();

        try {
            String statement = "UPDATE %s SET %s ".formatted(updateContext.getCollection(), updateContext.getFields().toString())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) : "");


            List<Object> context = new ArrayList<>(updateContext.getFields().values());
            context.addAll(updateContext.getContexts());

            ResultSet resultSet = searchFilter.isRequired() ?
                    session.execute(statement, context.toArray(new Object[0])) :
                    session.execute(statement);

            callback.succeed(OperationResult.Ok);
        } catch (Exception exception) {
            callback.failed(exception);
        }
    }

    @Override
    public void updateAsync(UpdateContext updateContext, PlaceholderContext placeholderContext, ResultCallback<OperationResult> callback) {
        ISearchFilter searchFilter = updateContext.getSearchFilter();

        try {
            String statement = "UPDATE %s SET %s ".formatted(updateContext.getCollection(), updateContext.getFields().toString())
                    + (searchFilter.isRequired() ? "WHERE " + searchFilter.toQuery(searchDictionary, placeholderContext) + " ALLOW FILTERING" : "");

            List<Object> context = new ArrayList<>(searchFilter.getContexts());
            context.addAll(updateContext.getFields().values());

            ResultSetFuture resultSet = searchFilter.isRequired() ?
                    session.executeAsync(statement, context.toArray(new Object[0])) :
                    session.executeAsync(statement);

            Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
                @Override
                public void onSuccess(ResultSet result) {
                    callback.succeed(OperationResult.Ok);
                }

                @Override
                public void onFailure(@NotNull Throwable exception) {
                    callback.failed(exception);
                }
            }, QUERY_EXECUTOR);
        } catch (Exception exception) {
            callback.failed(exception);
        }
    }

    @Override
    public void close() {
        session.close();
    }
}
