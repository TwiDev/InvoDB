package ch.twidev.invodb.drivers.statements;

import ch.twidev.invodb.exceptions.PrepareStatementException;
import ch.twidev.invodb.utils.Callback;

import java.sql.ResultSet;

public interface PreparedStatementConnection<P> {

    void asyncQuery(final String query, final Callback<?> callback, final Object... vars);

    void query(final String query, final Callback<?> callback, final Object... vars);

    void execute(final String query, final Object... vars);

    void asyncExecute(final String query, final Object... vars);


    P prepareStatement(String query, Object... vars) throws PrepareStatementException;

}
