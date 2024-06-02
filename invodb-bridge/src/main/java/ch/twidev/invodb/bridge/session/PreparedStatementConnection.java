package ch.twidev.invodb.bridge.session;


public interface PreparedStatementConnection<Statement> {

    Statement prepareStatement(String query, Object... vars);

}
