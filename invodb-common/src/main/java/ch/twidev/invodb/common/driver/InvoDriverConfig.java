package ch.twidev.invodb.common.driver;

import ch.twidev.invodb.bridge.driver.DriverConfig;
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

    public HashMap<EnvVar, Object> getConfig() {
        return config;
    }
}