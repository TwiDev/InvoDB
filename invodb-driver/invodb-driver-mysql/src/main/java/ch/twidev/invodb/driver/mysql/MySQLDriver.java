package ch.twidev.invodb.driver.mysql;

import ch.twidev.invodb.bridge.documents.ElementSet;
import ch.twidev.invodb.bridge.documents.OperationResult;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriver;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.operations.FindContext;
import ch.twidev.invodb.bridge.operations.InsertContext;
import ch.twidev.invodb.bridge.operations.UpdateContext;
import ch.twidev.invodb.bridge.placeholder.PlaceholderContext;
import ch.twidev.invodb.bridge.util.ResultCallback;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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
    public boolean exists() {
        return false;
    }


    @Override
    public ElementSet find(FindContext findOperationBuilder, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public CompletableFuture<ElementSet> findAsync(FindContext findOperationBuilder, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public OperationResult update(UpdateContext updateContext, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public CompletableFuture<OperationResult> updateAsync(UpdateContext updateContext, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public OperationResult insert(InsertContext updateContext, PlaceholderContext placeholderContext) {
        return null;
    }

    @Override
    public CompletableFuture<OperationResult> insertAsync(InsertContext updateContext, PlaceholderContext placeholderContext) {
        return null;
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
    public void close() {

    }
}
