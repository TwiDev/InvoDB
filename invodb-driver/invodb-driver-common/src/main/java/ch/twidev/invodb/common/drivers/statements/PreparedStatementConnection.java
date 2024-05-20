package ch.twidev.invodb.common.drivers.statements;

import ch.twidev.invodb.common.utils.Callback;
import ch.twidev.invodb.common.exceptions.PrepareStatementException;

public interface PreparedStatementConnection<P> {

    void asyncQuery(final String query, final Callback<?> callback, final Object... vars);

    void query(final String query, final Callback<?> callback, final Object... vars);

    void execute(final String query, final Object... vars);

    void asyncExecute(final String query, final Object... vars);


    P prepareStatement(String query, Object... vars) throws PrepareStatementException;

}
