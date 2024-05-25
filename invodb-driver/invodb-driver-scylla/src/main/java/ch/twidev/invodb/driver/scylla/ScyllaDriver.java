package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.driver.InvoDriver;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.session.DriverSession;
import com.datastax.driver.core.Session;

import java.util.concurrent.CompletableFuture;

public class ScyllaDriver extends InvoDriver<Session, DriverSession<Session>> {

    public ScyllaDriver(InvoDriverType invoDriverType) {
        super(invoDriverType);
    }

    @Override
    public DriverSession<Session> connectSession(String keyname) {
        return null;
    }

    @Override
    public CompletableFuture<DriverSession<Session>> asyncConnectSession(String keyname) {
        return null;
    }
}
