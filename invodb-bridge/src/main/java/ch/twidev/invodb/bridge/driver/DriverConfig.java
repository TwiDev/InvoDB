package ch.twidev.invodb.bridge.driver;

import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

public interface DriverConfig {

    Object get(EnvVar envVar) throws DriverConfigException;

    boolean isUrl();

    InvoDriverType getDriverType();

}
