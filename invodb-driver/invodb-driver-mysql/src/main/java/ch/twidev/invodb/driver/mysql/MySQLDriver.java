package ch.twidev.invodb.driver.mysql;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.driver.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriver;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.util.ThrowableCallback;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;

public class MySQLDriver extends InvoDriver<HikariDataSource> {
    public MySQLDriver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
        super(driverConfig, invoDriverType);
    }

    @Override
    public void initDriver() throws DriverConnectionException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DriverConnectionException(e);
        }


    }

    @Override
    public void find(FindContext findOperationBuilder, ThrowableCallback<ElementSet> throwableCallback) {

    }

    @Override
    public boolean exists() {
        return false;
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
    public void close() throws IOException {

    }
}
