package ch.twidev.invodb.driver.mysql;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.exceptions.PrepareStatementException;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.session.DriverSession;
import ch.twidev.invodb.bridge.session.PreparedStatementConnection;
import ch.twidev.invodb.bridge.util.ThrowableCallback;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.PreparedStatement;

public class MySQLConnection implements DriverSession<HikariDataSource>, PreparedStatementConnection<PreparedStatement> {
    @Override
    public void find(FindContext findOperationBuilder, ThrowableCallback<ElementSet> throwableCallback) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public HikariDataSource getLegacySession() {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String query, Object... vars) throws PrepareStatementException {
        return null;
    }

    @Override
    public void close() {

    }
}
