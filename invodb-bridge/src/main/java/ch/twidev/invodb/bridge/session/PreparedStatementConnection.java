package ch.twidev.invodb.bridge.session;


import ch.twidev.invodb.bridge.exceptions.PrepareStatementException;

public interface PreparedStatementConnection<Statement> {

    Statement prepareStatement(String query, Object... vars) throws PrepareStatementException;

}
