package ch.twidev.invodb.bridge.driver;

import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.session.DriverSession;

import java.util.concurrent.CompletableFuture;

public abstract class InvoClusterDriver<Session, Conn extends DriverSession<Session>> extends Driver {

    public InvoClusterDriver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
        super(driverConfig, invoDriverType);
    }

    public abstract Conn connectSession(String keyname);

    public abstract CompletableFuture<Conn> connectSessionAsync(String keyname);

}
