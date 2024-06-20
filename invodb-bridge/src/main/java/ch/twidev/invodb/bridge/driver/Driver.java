package ch.twidev.invodb.bridge.driver;

import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;

public abstract class Driver {

    private final InvoDriverType invoDriverType;

    private final DriverConfig driverConfig;

    private CachingProvider<?> queryCache;

    public Driver(DriverConfig driverConfig, InvoDriverType invoDriverType) {
        this.invoDriverType = invoDriverType;
        this.driverConfig = driverConfig;
    }

    public DriverConfig getDriverConfig() {
        return driverConfig;
    }

    public abstract void initDriver() throws DriverConnectionException;

    public abstract void close();

    public abstract boolean exists();

    public InvoDriverType getInvoDriverType() {
        return invoDriverType;
    }

    public CachingProvider<?> getQueryCache() {
        return queryCache;
    }

    public void setQueryCache(CachingProvider<?> queryCache) {
        this.queryCache = queryCache;
    }
}

