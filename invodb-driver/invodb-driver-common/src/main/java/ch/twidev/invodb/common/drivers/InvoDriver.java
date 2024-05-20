package ch.twidev.invodb.common.drivers;

import ch.twidev.invodb.common.exceptions.DriverConnectionException;
import ch.twidev.invodb.common.logs.DriverLogger;

public abstract class InvoDriver<C> {

    protected final DriverConfig driverConfig;

    private final DriverType driverType;
    public final DriverLogger logs;

    public C connection = null;

    public InvoDriver(DriverConfig driverConfig, DriverType driverType, String name) {
        this.driverConfig = driverConfig;
        this.driverType = driverType;

        this.logs = new DriverLogger(name);
    }

    protected void setConnection(C connection) {
        this.connection = connection;
    }

    public C getConnection() {
        return connection;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public DriverLogger getLog() {
        return logs;
    }

    public DriverConfig getDriverConfig() {
        return driverConfig;
    }

    public abstract void initConnection() throws DriverConnectionException;

    public abstract void closeConnection();

    public abstract boolean isConnected();

}
