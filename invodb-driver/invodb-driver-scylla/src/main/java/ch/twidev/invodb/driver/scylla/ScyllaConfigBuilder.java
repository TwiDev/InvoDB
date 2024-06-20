package ch.twidev.invodb.driver.scylla;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.driver.auth.AuthenticatorProvider;
import ch.twidev.invodb.bridge.driver.cluster.ContactPoint;
import ch.twidev.invodb.bridge.driver.cluster.ClusterPoints;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.config.DriverConfigBuilder;
import ch.twidev.invodb.bridge.environment.EnvVar;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;
import ch.twidev.invodb.bridge.util.Required;

import java.util.HashMap;

public class ScyllaConfigBuilder implements DriverConfigBuilder<ScyllaConfigBuilder, DriverConfig> {

    private final ClusterPoints contactPoints = new ClusterPoints();
    private String driverName;
    private AuthenticatorProvider authenticatorProvider;
    private Cache<?,?> cachingProvider;

    @Required
    public ScyllaConfigBuilder addContactPoint(ContactPoint contactPoint) {
        contactPoints.add(contactPoint);

        return this;
    }

    @Override
    public ScyllaConfigBuilder setQueryCache(Cache<?, ?> cachingProvider) {
        this.cachingProvider = cachingProvider;

        return this;
    }

    @Override
    public ScyllaConfigBuilder setAuthProvider(AuthenticatorProvider authenticatorProvider) {
        this.authenticatorProvider = authenticatorProvider;

        return this;
    }

    @Override
    public ScyllaConfigBuilder setDriverName(String driverName) {
        this.driverName = driverName;

        return this;
    }

    @Override
    public DriverConfig build() {
        return new DriverConfig() {

            private final HashMap<EnvVar, Object> config = new HashMap<>(){{
                put(EnvVar.SCYLLA_HOSTS, contactPoints);
                put(EnvVar.SCYLLA_AUTHENTICATOR, authenticatorProvider);
            }};

            @Override
            public Object get(EnvVar envVar) throws DriverConfigException {
                if(!config.containsKey(envVar))
                    throw new DriverConfigException();

                return config.get(envVar);
            }

            @Override
            public boolean isUrl() {
                return false;
            }

            @Override
            public Cache<?, ?> getQueryCache() {
                return cachingProvider;
            }

            @Override
            public InvoDriverType getDriverType() {
                return InvoDriverType.SCYLLA;
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
    public Cache<?,?> getQueryCache() {
        return null;
    }

    public ClusterPoints getContactPoints() {
        return contactPoints;
    }

    @Override
    public String getDriverName() {
        return driverName;
    }

    public Cache<?,?> getCachingProvider() {
        return cachingProvider;
    }
}
