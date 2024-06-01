package ch.twidev.invodb.bridge.exceptions;

import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.environment.EnvVar;

public class DriverConfigMissingException extends DriverConnectionException{

    public DriverConfigMissingException(EnvVar envVar, DriverConfig driverConfig) {
        super(String.format("%s environment variable is missing in the driver configuration %s", envVar.toString(), driverConfig.getDriverType().toString()));
    }
}
