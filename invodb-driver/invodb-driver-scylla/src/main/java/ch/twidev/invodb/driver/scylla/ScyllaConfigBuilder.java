package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

public class ScyllaConfigBuilder implements DriverConfig {

    

    @Override
    public Object get(EnvVar envVar) throws DriverConfigException {
        return null;
    }

    @Override
    public boolean isUrl() {
        return false;
    }

    @Override
    public InvoDriverType getDriverType() {
        return InvoDriverType.SCYLLA;
    }
}
