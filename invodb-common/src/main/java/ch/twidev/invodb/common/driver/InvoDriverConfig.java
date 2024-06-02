package ch.twidev.invodb.common.driver;

import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

import java.util.HashMap;

public class InvoDriverConfig implements DriverConfig {

    private HashMap<EnvVar, Object> config = new HashMap<>();

    public InvoDriverConfig(HashMap<EnvVar, Object> config) {
        this.config = config;
    }

    public InvoDriverConfig() {
    }

    @Override
    public Object get(EnvVar envVar) throws DriverConfigException {


        return config.get(envVar);
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
    public String getDriverName() {
        return null;
    }

    public HashMap<EnvVar, Object> getConfig() {
        return config;
    }
}
