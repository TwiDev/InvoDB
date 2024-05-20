package ch.twidev.invodb.common.exceptions;

import ch.twidev.invodb.common.drivers.DriverConfig;
import ch.twidev.invodb.common.environment.EnvVar;

public class DriverConfigMissingException extends DriverConnectionException{

    public DriverConfigMissingException(EnvVar envVar, DriverConfig driverConfig) {
        super(String.format("%s environment variable is missing in the driver configuration %s", envVar.toString(), driverConfig.getDriverType().toString()));
    }
}
