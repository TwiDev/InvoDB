package ch.twidev.invodb.common.drivers;

import ch.twidev.invodb.common.session.DriverConnection;

import java.util.concurrent.CompletableFuture;

public abstract class InvoDriver<Session extends DriverConnection<?>> {

    private final InvoDriverType driverType;

    private Session openedDriverConnection;

    public InvoDriver(InvoDriverType driverType) {
        this.driverType = driverType;
    }

    public InvoDriverType getDriverType() {
        return driverType;
    }

    public Session getOpenedDriverConnection() {
        return openedDriverConnection;
    }

    public void setOpenedDriverConnection(Session openedDriverConnection) {
        this.openedDriverConnection = openedDriverConnection;
    }

    public abstract Session connectSession(String keyname);
    public abstract CompletableFuture<Session> asyncConnectSession(String keyname);
}
