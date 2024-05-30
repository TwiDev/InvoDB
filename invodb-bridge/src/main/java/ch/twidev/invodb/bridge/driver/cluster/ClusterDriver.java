package ch.twidev.invodb.bridge.driver.cluster;

import ch.twidev.invodb.bridge.driver.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriver;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.session.DriverSession;

import java.util.concurrent.CompletableFuture;

public abstract class ClusterDriver<Session, Conn extends DriverSession<Session>> extends InvoDriver<Session> {

    public ClusterDriver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
        super(driverConfig, invoDriverType);
    }

    public abstract Conn connectSession(String keyname);

    public abstract CompletableFuture<Conn> asyncConnectSession(String keyname);

    public abstract void close();

    public abstract boolean isClosed();

}
