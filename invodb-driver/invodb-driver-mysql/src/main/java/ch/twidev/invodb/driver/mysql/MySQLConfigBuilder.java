package ch.twidev.invodb.driver.mysql;

import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.auth.AuthenticatorProvider;
import ch.twidev.invodb.bridge.driver.config.DriverConfigBuilder;
import ch.twidev.invodb.bridge.driver.config.URLDriverConfig;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

public class MySQLConfigBuilder implements DriverConfigBuilder<MySQLConfigBuilder, URLDriverConfig> {
    @Override
    public MySQLConfigBuilder setQueryCache(CachingProvider<?> cachingProvider) {
        return null;
    }

    @Override
    public MySQLConfigBuilder setAuthProvider(AuthenticatorProvider authenticatorProvider) {
        return null;
    }

    @Override
    public MySQLConfigBuilder setDriverName(String name) {
        return null;
    }

    @Override
    public URLDriverConfig build() {
        return new URLDriverConfig() {
            @Override
            public String getUrl() {
                return null;
            }

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
        };
    }

    @Override
    public AuthenticatorProvider getAuthProvider() {
        return null;
    }

    @Override
    public CachingProvider<?> getQueryCache() {
        return null;
    }

    @Override
    public String getDriverName() {
        return null;
    }

}
