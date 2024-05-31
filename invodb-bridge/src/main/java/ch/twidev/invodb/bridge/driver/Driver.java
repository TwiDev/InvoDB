package ch.twidev.invodb.bridge.driver;

import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;

import java.io.Closeable;

public abstract class Driver implements Closeable {

    private final InvoDriverType invoDriverType;

    private final DriverConfig driverConfig;

    public Driver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
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

}

