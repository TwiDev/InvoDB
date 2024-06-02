package ch.twidev.invodb.common.driver;

import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.config.URLDriverConfig;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

public class URLDriverBuilder implements URLDriverConfig {
    @Override
    public Object get(EnvVar envVar) throws DriverConfigException {
        return null;
    }

    @Override
    public InvoDriverType getDriverType() {
        return null;
    }

    @Override
    public String getDriverName() {
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }
}
