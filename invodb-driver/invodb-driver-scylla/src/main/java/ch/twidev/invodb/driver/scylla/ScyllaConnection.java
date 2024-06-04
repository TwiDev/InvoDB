package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.util.ThrowableCallback;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScyllaConnection implements DriverSession<Session> {

    private static final ExecutorService QUERY_EXECUTOR = Executors.newCachedThreadPool();

    private static final SearchDictionary searchDictionary = new SearchDictionary(){{
        put(SearchFilterType.ALL, new SearchCompositeParameter("*"));
        put(SearchFilterType.AND, new SearchCompositeParameter("AND"));
        put(SearchFilterType.OR, new SearchCompositeParameter("OR"));
        put(SearchFilterType.EQUAL, new SearchFieldParameter() {
            @Override
            public String parse(String key, Object value) {
                return key + " = '" + value + "'";
            }
        });
        put(SearchFilterType.NOT_EQUAL, new SearchFieldParameter() {
            @Override
            public String parse(String key, Object value) {
                return key + " != '" + value + "'";
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
    public void find(FindContext findOperationBuilder, ThrowableCallback<ElementSet> throwableCallback) {
        ElementSet elements = null;
        Throwable throwable = null;

        String searchQuery = findOperationBuilder.getSearchFilter().toQuery(searchDictionary);

        try {
            String statement =
                    "SELECT %s FROM %s ".formatted(findOperationBuilder.getAttributes().toString(), findOperationBuilder.getCollection())
                            + ((searchQuery == null) ? "" : "WHERE ?");

            ResultSet resultSet = searchQuery == null ? session.execute(statement) : session.execute(statement, searchQuery);

            elements = new ScyllaResultSet(resultSet);
        } catch (Exception exception) {
            throwable = exception;
        } finally {
            throwableCallback.run(elements, throwable);
        }

    }

    @Override
    public void findAsync(FindContext findOperationBuilder, ThrowableCallback<ElementSet> throwableCallback) {
        String searchQuery = findOperationBuilder.getSearchFilter().toQuery(searchDictionary);

        try {
            String statement =
                    "SELECT %s FROM %s ".formatted(findOperationBuilder.getAttributes().toString(), findOperationBuilder.getCollection())
                            + ((searchQuery == null) ? "" : "WHERE ?");

            ResultSetFuture resultSet = searchQuery == null ? session.executeAsync(statement) : session.executeAsync(statement, searchQuery);

            Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
                @Override
                public void onSuccess(ResultSet result) {
                    throwableCallback.run(new ScyllaResultSet(result), null);
                }

                @Override
                public void onFailure(Throwable t) {
                    throwableCallback.run(null, t);
                }
            }, QUERY_EXECUTOR);
        } catch (Exception exception) {
            throwableCallback.run(null, exception);
        }
    }

    @Override
    public void close() {
        session.close();
    }
}
