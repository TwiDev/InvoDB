package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;
import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.session.PreparedStatementConnection;
import ch.twidev.invodb.bridge.util.ThrowableCallback;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class ScyllaConnection implements DriverSession<Session>, PreparedStatementConnection<PreparedStatement> {

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
            PreparedStatement ps = this.prepareStatement(
                    "SELECT %s FROM %s ".formatted(findOperationBuilder.getAttributes().toString(), findOperationBuilder.getCollection())
                            + ((searchQuery == null) ? "" : "WHERE ?"));

            ResultSet resultSet = session.execute(
                            searchQuery == null ? ps.bind() : ps.bind(searchQuery)
                    );

            elements = new ScyllaResultSet(resultSet);
        } catch (Exception exception) {
            throwable = exception;
        } finally {
            System.out.println(elements);
            throwableCallback.run(elements, throwable);
        }

    }

    @Override
    public PreparedStatement prepareStatement(String query, Object... vars) {
        return session.prepare(query);
    }

    @Override
    public void close() {
        session.close();
    }
}
