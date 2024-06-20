package ch.twidev.invodb.driver.mongodb;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.auth.AuthenticatorProvider;
import ch.twidev.invodb.bridge.driver.config.DriverConfigBuilder;
import ch.twidev.invodb.bridge.driver.config.URLDriverConfig;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;

public class URLMongoConfigBuilder implements DriverConfigBuilder<URLMongoConfigBuilder, URLDriverConfig> {

    private final String url;

    private String driverName;
    private AuthenticatorProvider authenticatorProvider;
    private Cache<?,?> cachingProvider;

    public URLMongoConfigBuilder(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public URLMongoConfigBuilder setQueryCache(Cache<?,?> cachingProvider) {
        this.cachingProvider = cachingProvider;

        return this;
    }

    @Override
    @Deprecated
    public URLMongoConfigBuilder setAuthProvider(AuthenticatorProvider authenticatorProvider) {
        this.authenticatorProvider = authenticatorProvider;

        return this;
    }

    @Override
    public URLMongoConfigBuilder setDriverName(String driverName) {
        this.driverName = driverName;

        return this;
    }

    @Override
    public URLDriverConfig build() {
        return new URLDriverConfig() {

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public Object get(EnvVar envVar) throws DriverConfigException {
                return null;
            }

            @Override
            public Cache<?, ?> getQueryCache() {
                return cachingProvider;
            }

            @Override
            public InvoDriverType getDriverType() {
                return InvoDriverType.MONGODB;
            }

            @Override
            public String getDriverName() {
                return driverName;
            }
        };
    }

    @Override
    public AuthenticatorProvider getAuthProvider() {
        return authenticatorProvider;
    }

    @Override
    public Cache<?, ?> getQueryCache() {
        return cachingProvider;
    }

    public String getDriverName() {
        return driverName;
    }

    public AuthenticatorProvider getAuthenticatorProvider() {
        return authenticatorProvider;
    }
}
