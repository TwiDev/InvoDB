package ch.twidev.invodb.bridge.driver.config;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

public interface DriverConfig {

    Object get(EnvVar envVar) throws DriverConfigException;

    boolean isUrl();

    Cache<?,?> getQueryCache();

    InvoDriverType getDriverType();

    String getDriverName();
}
