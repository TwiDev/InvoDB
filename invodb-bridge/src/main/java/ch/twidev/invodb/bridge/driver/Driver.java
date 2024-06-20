package ch.twidev.invodb.bridge.driver;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;

public abstract class Driver {

    protected final InvoDriverType invoDriverType;

    protected final DriverConfig driverConfig;

    public Driver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
        this.invoDriverType = invoDriverType;
        this.driverConfig = driverConfig;
    }

    public DriverConfig getDriverConfig() {
        return driverConfig;
    }

    public Cache<?,?> getQueryCache() {
        return driverConfig.getQueryCache();
    }

    public abstract void initDriver() throws DriverConnectionException;

    public abstract void close();

    public abstract boolean exists();

    public InvoDriverType getInvoDriverType() {
        return invoDriverType;
    }
}

