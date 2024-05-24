package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.common.exceptions.PrepareStatementException;
import ch.twidev.invodb.common.query.builder.FindOperationBuilder;
import ch.twidev.invodb.common.query.operations.search.ObjectSearchFilter;
import ch.twidev.invodb.common.query.operations.search.SearchFilter;
import ch.twidev.invodb.common.query.operations.search.SearchFilterType;
import ch.twidev.invodb.common.query.operations.search.SubSearchFilter;
import ch.twidev.invodb.common.result.ResultSet;
import ch.twidev.invodb.common.session.DriverConnection;
import ch.twidev.invodb.common.statement.PreparedStatementConnection;
import ch.twidev.invodb.common.util.ThrowableCallback;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

import java.util.HashMap;

public class ScyllaConnection extends DriverConnection<Session> implements PreparedStatementConnection<PreparedStatement> {

    private final HashMap<SearchFilterType, String> CQL_OPERATORS = new HashMap<>(){{
        put(SearchFilterType.ALL, "*");
        put(SearchFilterType.AND, "and");
        put(SearchFilterType.OR, "or");
    }};

    public ScyllaConnection(Session session) {
        super(session);
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void find(FindOperationBuilder findOperationBuilder, ThrowableCallback<ResultSet> throwableCallback) {
        SearchFilter searchFilter = findOperationBuilder.getSearchFilter();


        PreparedStatement ps = this.prepareStatement("SELECT ? FROM ? WHERE ");
    }

    public String prepareSearch(SearchFilter searchFilter) {
        SearchFilterType searchFilterType = searchFilter.getSearchFilterType();
        if(!CQL_OPERATORS.containsKey(searchFilterType)) return "";

        String operator = CQL_OPERATORS.get(searchFilterType);

        if(searchFilter instanceof SubSearchFilter subSearchFilter) {
            StringBuilder stringBuilder = new StringBuilder(operator).append(" ");

            subSearchFilter.getSearchFilters().forEach(filter -> {
                stringBuilder.append(
                        prepareSearch(filter)
                );
            });

            return stringBuilder.toString();
        }else if(searchFilter instanceof ObjectSearchFilter objectSearchFilter) {
            return "%s=%s ".formatted(objectSearchFilter.getValue(), objectSearchFilter.getObject()/*TODO: format string, number,...*/);
        }

        return "";
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
