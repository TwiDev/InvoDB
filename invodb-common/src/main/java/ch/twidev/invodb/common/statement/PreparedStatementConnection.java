package ch.twidev.invodb.common.statement;

import ch.twidev.invodb.common.exceptions.PrepareStatementException;

public interface PreparedStatementConnection<Statement> {

    Statement prepareStatement(String query, Object... vars) throws PrepareStatementException;

}
