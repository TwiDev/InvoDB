package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.exceptions.PrepareStatementException;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.session.ISession;
import ch.twidev.invodb.bridge.contexts.SearchDictionary;
import ch.twidev.invodb.bridge.contexts.SearchFilterType;

import ch.twidev.invodb.bridge.statement.PreparedStatementConnection;
import ch.twidev.invodb.bridge.util.ThrowableCallback;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

public class ScyllaConnection implements ISession<Session>, PreparedStatementConnection<PreparedStatement> {

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
                return key + " = '" + value + "'";
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
    public void find(FindContext findOperationBuilder, ThrowableCallback<Object> throwableCallback) {
        PreparedStatement ps = this.prepareStatement("SELECT ? FROM ? WHERE ?",
                findOperationBuilder.getAttributes().toString(),
                findOperationBuilder.getCollection(),
                findOperationBuilder.getSearchFilter().toQuery(searchDictionary));
    }

    @Override
    public PreparedStatement prepareStatement(String query, Object... vars) throws PrepareStatementException {
        try {
            return session.prepare(new SimpleStatement(query, vars));
        } catch (Exception cause) {
            throw new PrepareStatementException(cause);
        }
    }
}
