package ch.twidev.invodb.drivers.driver;

import ch.twidev.invodb.drivers.DriverConfig;
import ch.twidev.invodb.drivers.DriverType;
import ch.twidev.invodb.drivers.InvoDriver;
import ch.twidev.invodb.exceptions.DriverConnectionException;
import com.datastax.driver.core.Session;
import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public class InvoSessionDriver extends InvoDriver<CqlSession> {

    public static InvoSessionDriver fromScyllaSession(Session session) {
        return null;
    }

    public InvoSessionDriver(DriverConfig driverConfig, DriverType driverType, String name) {
        super(driverConfig, driverType, name);
    }

    @Override
    public void initConnection() throws DriverConnectionException {
        CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("1.2.3.4", 9042))
                .addContactPoint(new InetSocketAddress("5.6.7.8", 9042))
                .withLocalDatacenter("datacenter1")
                .build();

        this.setConnection(session);
    }

    @Override
    public void closeConnection() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
