package ch.twidev.invodb.driver.mysql;

import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.config.URLDriverConfig;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

public class MySQLConfigBuilder implements URLDriverConfig {
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
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }
}
