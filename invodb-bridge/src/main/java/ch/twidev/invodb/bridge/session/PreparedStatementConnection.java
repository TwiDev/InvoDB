package ch.twidev.invodb.bridge.session;


import com.google.common.util.concurrent.ListenableFuture;

public interface PreparedStatementConnection<Statement> {

    Statement prepareStatement(String query, Object... vars);
    ListenableFuture<Statement> prepareStatementAsync(String query, Object... vars);

}
