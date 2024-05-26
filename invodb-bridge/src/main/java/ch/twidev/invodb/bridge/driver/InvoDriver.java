package ch.twidev.invodb.bridge.driver;

import ch.twidev.invodb.bridge.session.DriverSession;

import java.util.concurrent.CompletableFuture;

public abstract class InvoDriver<Session, Conn extends DriverSession<Session>> {

    private final InvoDriverType invoDriverType;

    private DriverSession<Session> currentSession = null;

    public InvoDriver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
        this.invoDriverType = invoDriverType;
    }

    public abstract Conn connectSession(String keyname);
    public abstract CompletableFuture<Conn> asyncConnectSession(String keyname);

    public InvoDriverType getInvoDriverType() {
        return invoDriverType;
    }

    public DriverSession<Session> getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(DriverSession<Session> currentSession) {
        this.currentSession = currentSession;
    }
}
