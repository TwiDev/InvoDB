package ch.twidev.invodb.bridge.driver.config;

import ch.twidev.invodb.bridge.cache.Cache;
import ch.twidev.invodb.bridge.driver.auth.AuthenticatorProvider;

public interface DriverConfigBuilder<Builder, Config extends DriverConfig> {

    Builder setQueryCache(Cache<?,?> cachingProvider);

    Builder setAuthProvider(AuthenticatorProvider authenticatorProvider);

    Builder setDriverName(String name);

    Config build();

    AuthenticatorProvider getAuthProvider();

    Cache<?,?> getQueryCache();

    String getDriverName();

}
