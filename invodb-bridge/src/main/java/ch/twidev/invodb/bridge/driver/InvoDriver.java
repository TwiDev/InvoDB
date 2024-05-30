package ch.twidev.invodb.bridge.driver;

import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import ch.twidev.invodb.bridge.session.DriverSession;

public abstract class InvoDriver<Session> {

    private final InvoDriverType invoDriverType;

    private final DriverConfig driverConfig;

    private DriverSession<Session> currentSession = null;

    public InvoDriver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
        this.invoDriverType = invoDriverType;
        this.driverConfig = driverConfig;
    }

    public DriverConfig getDriverConfig() {
        return driverConfig;
    }

    public abstract void initDriver() throws DriverConnectionException;

    public abstract boolean exists();

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
