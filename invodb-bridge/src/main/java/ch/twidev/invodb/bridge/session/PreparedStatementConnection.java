package ch.twidev.invodb.bridge.session;

import ch.twidev.invodb.common.exceptions.PrepareStatementException;

public interface PreparedStatementConnection<Statement> {

    Statement prepareStatement(String query, Object... vars) throws PrepareStatementException;

}
