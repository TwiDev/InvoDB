package ch.twidev.invodb.bridge.driver;

import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.session.DriverSession;

public abstract class InvoDriver<Session> extends Driver implements DriverSession<Session> {


    private Session currentSession = null;

    public InvoDriver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
        super(driverConfig, invoDriverType);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }
}
