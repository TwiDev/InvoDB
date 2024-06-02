package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.config.DriverConfigBuilder;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

public class ScyllaConfigBuilder implements DriverConfigBuilder<ScyllaConfigBuilder> {

    @Override
    public ScyllaConfigBuilder setDriverType(InvoDriverType invoDriverType) {
        return null;
    }

    @Override
    public DriverConfig build() {
        return null;
    }
}
